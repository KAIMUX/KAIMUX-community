package lt.itsvaidas.ZCAPI;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class RegisterPlaceholders {
	
	private static PlaceholderExpansion placeholder = null;
	
	public static void register() {
		if (!PlaceholderAPI.getRegisteredIdentifiers().contains("zcmsg")) {
			placeholder = new PlaceholderExpansion() {
				
				@Override
				public String getVersion() {
					return "1.0";
				}
				
				@Override
				public String getIdentifier() {
					return "zcmsg";
				}
				
				@Override
				public String getAuthor() {
					return "AsVaidas";
				}
	
				@Override
				public String onPlaceholderRequest(Player p, String params) {
					return null;
				}
			};
			placeholder.register();
		}
	}
	
	public static void unregister() {
		placeholder.unregister();
	}
}
