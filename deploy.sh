#!/bin/bash

current_dir=$(dirname "$0")
cd "$current_dir" || exit 1

ENV="$GITHUB_BRANCH"

echo "Deploying to $ENV environment..."

# Load the SSH key
if [ -z "$SSH_KEY" ]; then
    echo "SSH_KEY is not set"
    exit 1
fi
echo "$SSH_KEY" > /tmp/deploy_key
chmod 600 /tmp/deploy_key

directories=()

mkdir "$current_dir/tmp_plugins"

while IFS= read -r -d '' dir; do
    dir=$(echo "$dir" | sed 's#^\./##')
    if [ ! -d "$current_dir/$dir" ]; then
        continue
    fi
    if [ ! -f "$current_dir/$dir/target_paths" ]; then
        continue
    fi
    plugins_dir="$current_dir/$dir/build/libs"
    if [ ! -f "$plugins_dir/$dir.jar" ]; then
        echo "File $plugins_dir/$dir.jar does not exist, skipping"
        continue
    fi
    cp "$plugins_dir/$dir.jar" "$current_dir/tmp_plugins/$dir.jar" || exit 1
    directories+=("$dir")
done < <(find "$current_dir" -mindepth 1 -maxdepth 1 -type d -print0)

zip -r "$current_dir/tmp_plugins.zip" "$current_dir/tmp_plugins"

scp -oStrictHostKeyChecking=no -i /tmp/deploy_key -P "$SERVER_PORT" "$current_dir/tmp_plugins.zip" "$SERVER_USER@$SERVER_IP:/tmp/tmp_plugins.zip" || exit 1
ssh -oStrictHostKeyChecking=no -i /tmp/deploy_key -p "$SERVER_PORT" "$SERVER_USER@$SERVER_IP" "unzip -o /tmp/tmp_plugins.zip -d /tmp/; rm /tmp/tmp_plugins.zip" || exit 1

full_command=""

for plugin in "${directories[@]}"; do
    while IFS= read -r line || [[ -n "$line" ]]; do
        if [ -z "$line" ]; then
            continue
        fi

        # Substitute {ENV} placeholder in the path
        server_dir=$(echo "$line" | sed "s#{ENV}#$ENV#g")
        full_command="$full_command cp /tmp/tmp_plugins/$plugin.jar $server_dir/;"

    done < "$current_dir/$plugin/target_paths"
done

full_command="$full_command rm -rf /tmp/tmp_plugins"

echo "Full copy command: $full_command"

ssh -oStrictHostKeyChecking=no -i /tmp/deploy_key -p "$SERVER_PORT" "$SERVER_USER@$SERVER_IP" "$full_command" || exit 1

echo "Successfully deployed all plugins to $ENV environment"
