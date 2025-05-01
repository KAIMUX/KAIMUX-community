package lt.itsvaidas.CommandsAPI;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lt.itsvaidas.CommandsAPI.anotations.Argument;
import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.CommandsAPI.enums.ArgumentType;
import lt.itsvaidas.CommandsAPI.exceptions.CommandExecuteException;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.GlobalMessages;
import lt.itsvaidas.ZCAPI.tools.PlayerUUID;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class CommandRegister {

    private static class PathSegment {
        private final String segment;
        private final boolean isArgument;
        private final ArgumentType argumentType;
        private final Map<String, PathSegment> subCommands;
        private final @Nullable Class<? extends ArgumentProvider> provider;
        private String permission;
        private Method method;
        private Object clazz;

        public PathSegment(
                String segment,
                boolean isArgument,
                ArgumentType argumentType,
                Map<String, PathSegment> subCommands,
                @Nullable Class<? extends ArgumentProvider> provider,
                @Nullable Object clazz
        ) {
            this.segment = segment;
            this.isArgument = isArgument;
            this.argumentType = argumentType;
            this.subCommands = subCommands;
            this.provider = provider;
            this.clazz = clazz;
        }

        public String getSegment() {
            return segment;
        }

        public boolean isArgument() {
            return isArgument;
        }

        public ArgumentType getArgumentType() {
            return argumentType;
        }

        public Map<String, PathSegment> getSubCommands() {
            return subCommands;
        }

        public @Nullable Class<? extends ArgumentProvider> getProvider() {
            return provider;
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public Object getClazz() {
            return clazz;
        }

        @Override
        public String toString() {
            return "PathSegment{" +
                    "segment='" + segment + '\'' +
                    ", isArgument=" + isArgument +
                    ", provider=" + provider +
                    ", permission='" + permission + '\'' +
                    ", method=" + method +
                    ", subCommands=" + subCommands.values().stream().map(PathSegment::toString).collect(Collectors.joining(", ")) +
                    '}';
        }
    }

    private final Plugin plugin;
    private final LifecycleEventManager<@NotNull Plugin> manager;

    public CommandRegister(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getLifecycleManager();
    }


    public void register(Object clazz) {
        if (clazz.getClass().isAnnotationPresent(Command.class)) {
            Command commandAnnotation = clazz.getClass().getAnnotation(Command.class);

            // Extract class-level metadata
            String baseCommand = commandAnnotation.name();
            String description = commandAnnotation.description();
            String[] aliases = commandAnnotation.aliases();
            String permission = commandAnnotation.permission();

            LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(baseCommand);

            if (!permission.equalsIgnoreCase("")) {
                root.requires(source -> !(source.getSender() instanceof Player player) || player.hasPermission(permission));
            }

            Map<String, PathSegment> pathSegments = new HashMap<>();
            boolean hasDefault = false;
            String defaultDescription = null;

            for (Method method : clazz.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Path.class)) {
                    Path executable = method.getAnnotation(Path.class);
                    String methodDescription = executable.description();

                    if (executable.path().equalsIgnoreCase("")) {
                        root.executes(context -> executeCommand(clazz, method, context));
                        hasDefault = true;
                        defaultDescription = methodDescription;
                        continue;
                    }

                    String[] paths = executable.path().split(" ");
                    String methodPermission = executable.permission();
                    Map<String, Argument> arguments = Arrays.stream(executable.arguments()).map(arg -> Map.entry(arg.key(), arg)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    Map<String, PathSegment> currentSubCommands = pathSegments;
                    PathSegment currentSegment = null;

                    int argIndex = 1;
                    for (int i = 0; i < paths.length; i++) {
                        String path = paths[i];
                        currentSegment = currentSubCommands.get(path);
                        if (currentSegment != null) {
                            if (path.startsWith("<") && path.endsWith(">") || path.startsWith("[") && path.endsWith("]")) {
                                argIndex++;
                            }
                            currentSubCommands = currentSegment.getSubCommands();

                            if (methodPermission.equalsIgnoreCase("")) {
                                currentSegment.setPermission(methodPermission);
                            }
                        } else {
                            Map<String, PathSegment> newSubCommands = new HashMap<>();
                            if (path.startsWith("<") && path.endsWith(">")) { // Required argument
                                String argumentName = path.substring(1, path.length() - 1).toLowerCase();
                                String argument = "arg" + argIndex++;
                                Argument methodArgument = arguments.get(argumentName.toLowerCase());
                                Class<? extends ArgumentProvider> provider = methodArgument != null ? methodArgument.provider() : null;
                                ArgumentType type = methodArgument != null ? methodArgument.type() : ArgumentType.SINGLE;

                                currentSegment = new PathSegment(argument, true, type, newSubCommands, provider, clazz);
                                currentSubCommands.put(path, currentSegment);
                            } else if (path.startsWith("[") && path.endsWith("]")) { // Optional argument
                                String argumentName = path.substring(1, path.length() - 1).toLowerCase();
                                String argument = "arg" + argIndex++;
                                Argument methodArgument = arguments.get(argumentName.toLowerCase());
                                Class<? extends ArgumentProvider> provider = methodArgument != null ? methodArgument.provider() : null;
                                ArgumentType type = methodArgument != null ? methodArgument.type() : ArgumentType.SINGLE;

                                currentSegment = new PathSegment(argument, true, type, newSubCommands, provider, clazz);
                                currentSubCommands.put(path, currentSegment);
                            } else { // Literal path
                                currentSegment = new PathSegment(path.toLowerCase(), false, ArgumentType.SINGLE, newSubCommands, null, clazz);
                                currentSubCommands.put(path, currentSegment);
                            }

                            currentSegment.setPermission(methodPermission);

                            if (paths.length > i + 1 && paths[i + 1].startsWith("[") && paths[i + 1].endsWith("]")) {
                                currentSegment.setMethod(method);
                            }

                            currentSubCommands = newSubCommands;
                        }
                    }

                    currentSegment.setMethod(method);
                }
            }

            for (String key : pathSegments.keySet()) {
                PathSegment segment = pathSegments.get(key);
                recursiveCommandRegistering(baseCommand, root, segment);
            }

            String finalDefaultDescription = defaultDescription;
            root.then(Commands.literal("help").executes(context -> executeHelp(finalDefaultDescription, pathSegments, baseCommand, context)));

            if (!hasDefault) {
                root.executes(context -> executeHelp(pathSegments, baseCommand, context));
            }

            // Register the command with the CommandAPI
            manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
                final Commands commands = event.registrar();
                commands.register(root.build(), description, Arrays.asList(aliases));
            });
        }
    }

    private void recursiveCommandRegistering(String baseCommand, LiteralArgumentBuilder<CommandSourceStack> root, PathSegment segment) {
        String name = segment.getSegment();

        if (segment.isArgument()) {
            RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder = getCommandSourceStackRequiredArgumentBuilder(segment, name);

            for (String key : segment.getSubCommands().keySet()) {
                PathSegment subSegment = segment.getSubCommands().get(key);
                recursiveCommandRegistering(baseCommand, argumentBuilder, subSegment);
            }

            if (segment.getMethod() != null) {
                argumentBuilder.executes(context -> executeCommand(segment.clazz, segment.getMethod(), context));
            } else {
                argumentBuilder.executes(context -> executeHelp(segment.getSubCommands(), baseCommand, context));
            }

            root.then(argumentBuilder);

        } else {
            LiteralArgumentBuilder<CommandSourceStack> literalBuilder = getCommandSourceStackLiteralArgumentBuilder(segment, name);

            for (String key : segment.getSubCommands().keySet()) {
                PathSegment subSegment = segment.getSubCommands().get(key);
                recursiveCommandRegistering(baseCommand, literalBuilder, subSegment);
            }

            if (segment.getMethod() != null) {
                literalBuilder.executes(context -> executeCommand(segment.getClazz(), segment.getMethod(), context));
            } else {
                literalBuilder.executes(context -> executeHelp(segment.getSubCommands(), baseCommand, context));
            }

            root.then(literalBuilder);
        }
    }

    private void recursiveCommandRegistering(String baseCommand, RequiredArgumentBuilder<CommandSourceStack, ?> root, PathSegment segment) {
        String name = segment.getSegment();

        if (segment.isArgument()) {
            RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder = getCommandSourceStackRequiredArgumentBuilder(segment, name);

            for (String key : segment.getSubCommands().keySet()) {
                PathSegment subSegment = segment.getSubCommands().get(key);
                recursiveCommandRegistering(baseCommand, argumentBuilder, subSegment);
            }

            if (segment.getMethod() != null) {
                argumentBuilder.executes(context -> executeCommand(segment.getClazz(), segment.getMethod(), context));
            } else {
                argumentBuilder.executes(context -> executeHelp(segment.getSubCommands(), baseCommand, context));
            }

            root.then(argumentBuilder);

        } else {
            LiteralArgumentBuilder<CommandSourceStack> literalBuilder = getCommandSourceStackLiteralArgumentBuilder(segment, name);

            for (String key : segment.getSubCommands().keySet()) {
                PathSegment subSegment = segment.getSubCommands().get(key);
                recursiveCommandRegistering(baseCommand, literalBuilder, subSegment);
            }

            if (segment.getMethod() != null) {
                literalBuilder.executes(context -> executeCommand(segment.getClazz(), segment.getMethod(), context));
            } else {
                literalBuilder.executes(context -> executeHelp(segment.getSubCommands(), baseCommand, context));
            }

            root.then(literalBuilder);
        }
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> getCommandSourceStackLiteralArgumentBuilder(PathSegment segment, String name) {
        LiteralArgumentBuilder<CommandSourceStack> literalBuilder = Commands.literal(name);
        if (segment.getPermission() != null && !segment.getPermission().equalsIgnoreCase("")) {
            literalBuilder.requires(source -> !(source.getSender() instanceof Player player) || player.hasPermission(segment.getPermission()));
        }
        return literalBuilder;
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, ?> getCommandSourceStackRequiredArgumentBuilder(PathSegment segment, String name) {
        ArgumentType argumentType = segment.getArgumentType();
        com.mojang.brigadier.arguments.ArgumentType<?> finalArgumentType = switch (argumentType) {
            case SINGLE -> StringArgumentType.string();
            case NUMBER -> IntegerArgumentType.integer();
            case DOUBLE -> DoubleArgumentType.doubleArg();
            case SENTENCE -> StringArgumentType.greedyString();
            case BOOLEAN -> BoolArgumentType.bool();
        };

        RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder = Commands.argument(name, finalArgumentType);
        if (segment.getProvider() != null) {
            argumentBuilder.suggests((context, builder) -> getSuggestions(segment.getProvider(), context, builder));
        }
        if (segment.getPermission() != null && !segment.getPermission().equalsIgnoreCase("")) {
            argumentBuilder.requires(source -> !(source.getSender() instanceof Player player) || player.hasPermission(segment.getPermission()));
        }
        return argumentBuilder;
    }

    private CompletableFuture<Suggestions> getSuggestions(Class<? extends ArgumentProvider> argumentProvider, CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            // Instantiate the ArgumentProvider and fetch suggestions
            ArgumentProvider provider = argumentProvider.getDeclaredConstructor().newInstance();
            Stream<String> provided = provider.provide(context.getSource());
            provided.filter(s -> s.toLowerCase().startsWith(builder.getRemainingLowerCase())).forEach(builder::suggest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to resolve suggestions", e);
        }
        return builder.buildFuture();
    }

    private int executeCommand(Object clazz, Method method, CommandContext<CommandSourceStack> context) {
        try {
            // Extract arguments from the CommandContext and invoke the method
            CommandSourceStack source = context.getSource();
            Object[] parameters = new Object[method.getParameterCount()];

            for (int i = 0; i < method.getParameterCount(); i++) {
                Class<?> parameterType = method.getParameterTypes()[i];
                try {
                    if (i == 0) {
                        if (parameterType.equals(CommandSender.class)) {
                            parameters[i] = source.getSender();
                        } else if (parameterType.equals(Player.class)) {
                            if (source.getSender() instanceof Player player) {
                                parameters[i] = player;
                            } else {
                                throw new CommandExecuteException(GlobalMessages.ERROR__PLAYER_ONLY.getTitle());
                            }
                        }
                        continue;
                    }
                    if (parameterType.equals(CommandSender.class)) {
                        parameters[i] = source.getSender();
                    } else if (parameterType.equals(Player.class)) {
                        Player player = Bukkit.getPlayer(context.getArgument(method.getParameters()[i].getName(), String.class));
                        if (player == null)
                            throw new CommandExecuteException(GlobalMessages.ERROR__PLAYER_NOT_ONLINE.getTitle());
                        parameters[i] = player;
                    } else if (parameterType.equals(OfflinePlayer.class)) {
                        UUID uuid = PlayerUUID.getUUID(context.getArgument(method.getParameters()[i].getName(), String.class));
                        if (uuid == null)
                            throw new CommandExecuteException(GlobalMessages.ERROR__PLAYER_NOT_FOUND.getTitle());
                        parameters[i] = Bukkit.getOfflinePlayer(uuid);
                    } else if (parameterType.isEnum()) {
                        parameters[i] = Enum.valueOf((Class<Enum>) parameterType, context.getArgument(method.getParameters()[i].getName(), String.class));
                    } else if (parameterType.equals(Enchantment.class)) {
                        Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(Key.key(context.getArgument(method.getParameters()[i].getName(), String.class)));
                        if (enchantment == null)
                            throw new CommandExecuteException(GlobalMessages.ERROR__ENCHANTMENT_NOT_FOUND.getTitle());
                        parameters[i] = enchantment;
                    } else if (parameterType.equals(World.class)) {
                        World world = Bukkit.getWorld(context.getArgument(method.getParameters()[i].getName(), String.class));
                        if (world == null)
                            throw new CommandExecuteException(GlobalMessages.ERROR__WORLD_NOT_FOUND.getTitle());
                        parameters[i] = world;
                    } else {
                        parameters[i] = context.getArgument(method.getParameters()[i].getName(), parameterType);
                    }
                } catch (Exception e) {
                    if (e.getMessage() != null && !e.getMessage().startsWith("No such argument") && e instanceof IllegalArgumentException) {
                        MSG.Send.error(source.getSender(), GlobalMessages.INCORRECT_PARAMETER.getTitle(), method.getParameters()[i].getName());
                    }

                    if (e instanceof CommandExecuteException argumentNotFoundException) {
                        MSG.Send.error(source.getSender(), argumentNotFoundException.getMessage());
                        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                    }

                    if (parameterType.equals(int.class)) {
                        parameters[i] = 0;
                    } else if (parameterType.equals(boolean.class)) {
                        parameters[i] = false;
                    } else if (parameterType.equals(double.class)) {
                        parameters[i] = 0.0;
                    } else if (parameterType.equals(float.class)) {
                        parameters[i] = 0.0f;
                    } else if (parameterType.equals(long.class)) {
                        parameters[i] = 0L;
                    } else if (parameterType.equals(short.class)) {
                        parameters[i] = (short) 0;
                    } else if (parameterType.equals(byte.class)) {
                        parameters[i] = (byte) 0;
                    } else if (parameterType.equals(char.class)) {
                        parameters[i] = ' ';
                    } else {
                        parameters[i] = null;
                    }
                }
            }

            method.invoke(clazz, parameters);
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Failure
        }
    }

    private int executeHelp(String defaultDescription, Map<String, PathSegment> segments, String baseCommand, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (defaultDescription != null && context.getInput().toLowerCase().endsWith("help")) {
            MSG.command(source.getSender(), "/" + context.getInput().replace(" help", ""), defaultDescription);
        }
        showHelp(source.getSender(), baseCommand, context.getInput(), segments);

        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
    }

    private int executeHelp(Map<String, PathSegment> segments, String baseCommand, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        showHelp(source.getSender(), baseCommand, context.getInput(), segments);

        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
    }

    private void showHelp(CommandSender sender, String baseCommand, String label, Map<String, PathSegment> segments) {
        List<String> sortedSubCommands = segments.keySet().stream()
            .sorted()
            .toList();

        String prefix = label.replace(" help", "");

        for (String key : sortedSubCommands) {
            PathSegment segment = segments.get(key);

            if (!segment.getPermission().equalsIgnoreCase("") && !sender.hasPermission(segment.getPermission())) {
                continue;
            }

            if (segment.getSubCommands().isEmpty()) {
                Method method = segment.getMethod();
                Path executable = method.getAnnotation(Path.class);
                String description = executable.description();
                MSG.command(sender, "/" + prefix + " " + key, description);
            } else {
                showHelp(sender, baseCommand, prefix + " " + key, segment.getSubCommands());
            }
        }
    }
}
