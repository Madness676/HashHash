#!/bin/bash

githubToken=$1
gistId=$2
username="Madness676"
repositoryName="HashHash"
filePath="new.txt"

getLatestGistCommitHash() {
    local gistId=$1
    local responseBody
    responseBody=$(curl -s "https://api.github.com/gists/$gistId")
    if [[ $? -eq 0 ]]; then
        local latestCommitHash=$(echo "$responseBody" | jq -r '.history[0].version')
        echo "$latestCommitHash"
    else
        return 1
    fi
}

latestCommitHash=$(getLatestGistCommitHash "$gistId")

if [[ -n "$latestCommitHash" ]]; then
    fileUrl="https://api.github.com/repos/$username/$repositoryName/contents/$filePath"
    fileExist=$(curl -s -o /dev/null -w "%{http_code}" "$fileUrl")
    if [[ $fileExist -eq 200 ]]; then
        # If the file exists, update its content
        curl -X PUT \
             -H "Authorization: token $githubToken" \
             -d '{"message": "Updating file via GitHub Actions", "content": "'"$latestCommitHash"'", "sha": "'"$fileSha"'"}' \
             "$fileUrl"
        echo "File updated successfully!"
    else
        # If the file doesn't exist, create it
        curl -X PUT \
             -H "Authorization: token $githubToken" \
             -d '{"message": "Creating file via GitHub Actions", "content": "'"$latestCommitHash"'"}' \
             "$fileUrl"
        echo "File created successfully!"
    fi
else
    echo "Failed to retrieve latest commit hash of the Gist."
fi
