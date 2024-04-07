This package is designed to organize a list of products into the largest possible groups (groups contain products that can be shipped by the same delivery method).

## BasketSplitter
This class is responsible for dividing the list of products into splits. It is initialized by providing the path to the configuration file.

## ConfigManager
This class manages the data from the configuration file (config.json). It's worth noting that it converts products and delivery methods into the Integer type, which allows for efficient comparison between them.

## Basket
This class manages data from the input list. It represents the input list of products as a bipartite graph, where edges connect products with their available delivery methods.

[//]: # (this file is made with chat GPT help.URL to chat: https://chat.openai.com/share/1c9e715e-af5e-44a1-b0d9-868e62a9af41)