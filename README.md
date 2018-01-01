# Java Json Mapper

Java class to convert Json string to map.

### Usage

```gradle
JsonMapper mapper = new JsonMapper("{\"id\": 1, \"name\": \"John Doe\"}");
HashMap map = (HashMap) mapper.map();

System.out.println(map);
```

### Output
```gradle
{name=John Doe, id=1}
```
