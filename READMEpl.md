Pakiet ten jest przeznaczony do organizowania listy produktów w jak największe grupy (grupy zawierają produkty, które mogą być wysyłane tą samą metodą dostawy).

## BasketSplitter
Klasa odpowiedzialna za podział listy pakietów na grupy. Jest inicjalizowana przez podanie ścieżki do pliku konfiguracyjnego.

## ConfigManager
Klasa zarządzająca danymi z pliku konfiguracyjnego (config.json). Warto podkreślić, że konwertuje ona produkty i metody dostawy na typ Integer, co umożliwia efektywne porównywanie ich między sobą.

## Basket
Klasa zarządzająca danymi z listy wejściowej. Reprezentuje wejściową listę produktów jako graf dwudzielny, w którym krawędzie łączą produkty z dostępnymi dla nich metodami dostawy.