# Лабораторная работа 4

век с полями

    ID
    Имя
    Пол
    Подразделение(Сущность подразделение)
    Зарплата
    Дата рождения

Подразделение с полями:

    ID(генерится в програме)
    Название

Дан CSV файл(архив с ним есть внутри задания), который содержит в себе информацию о людях. Нужно считать данные о людях
из этого файла в список
В этой задаче нужно пользоваться встроенными Java коллекциями
Для работы с CSV файлом рекомендую использовать библиотеку opencsv(НО можете и без нее - это на ваше усмотрение)
Ее можно либо скачать в виде jar файла и подключить к проекту если не используете maven, либо подключить как maven
зависимость
Чтение из файла с помощью этой библиотеки может выглядеть так:

```java
try(InputStream in = getClass().getClassLoader().getResourceAsStream(csvFilePath);
CSVReader reader = in == null ? null : new CSVReader(new InputStreamReader(in), separator)){
        if(reader ==null){
        throw new

FileNotFoundException(csvFilePath);
    }
String[] nextLine;
    while((nextLine =reader.

readNext())!=null){
//А тут работаете с nextLine котрый представляет из себя текущую строчку в файле, уже разбитую на массив по разделителю
separator
//Попробуйте просто вывести на экран этот nextLine и, думаю, все будет понятно
    }

```

csvFilePath - путь к файлу. Файл закидывайте в ресурсы. Если не получится, то укажите уж тогда полный путь.
separator - разделитель. В нашем случае - ';'

Т.е. на выходе у вас должен получиться объект типа List в котором будут находится люди из файла. Т.е. нужно не просто
прочитать файл и вывести его на экран, а именно получить список
ID подразделения можете сами генерировать