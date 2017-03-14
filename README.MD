# JSONReflection for java
## Create by SKAIBlue

### Before using. 사용하기 전에
This library need java-json library.<br>
You can download from [here](https://github.com/SKAIBlue/JSONReflection/raw/master/lib/java-json.jar).<br><br>
이 라이브러리는 java-json 라이브러리가 필요합니다.<br>
[여기](https://github.com/SKAIBlue/JSONReflection/raw/master/lib/java-json.jar)에서 다운받을 수 있습니다.

You can download this library from [here](https://github.com/SKAIBlue/JSONReflection/raw/master/lib/JSONReflection.jar)<br><br>
이 라이브러리는 [여기](https://github.com/SKAIBlue/JSONReflection/raw/master/lib/JSONReflection.jar)에서 다운받을 수 있습니다.


### Usage 사용법
First, You have to import package.<br><br>
먼저, 패키지를 임포트 해야합니다.
```JAVA
import com.inursoft.jsonreflection;
```

This library provide just four methods.<br><br>
이 라이브러리는 단 4개의 메소드를 제공합니다.

```JAVA
public static JSONObject JSON.toJSONObject(Object o);
```
This method convert object to JSONObject<br>
이 메소드는 오브젝트를 JSONObject 로 변환합니다.
```JAVA
public static JSONArray JSON.toJSONArray(List list);
```

This method convert List to JSONArray<br>
이 메소드는 오브젝트를 JSONArray 로 변환합니다.
```JAVA
public static Object JSON.toObject(Class t, String json);
```


This method convert JSON string to Object of Class t<br>
이 메소드는 JSON 형식의 문자열을 Class t 타입의 오브젝트로 변환합니다.
```JAVA
public static List JSON.toList(Class t, String json);
```
This method convert JSON string to List of Class t<br>
이 메소드는 JSONArray 형식의 문자열을 Class t 타입을 가지는 리스트로 변환합니다.

### Example 예제
#### Test
```JAVA
public class Test {
    // int
    public Integer i;
    // double
    public Double d;
    // string
    public String s;
    // long
    public Long l;
    // boolean
    public Boolean b;
    // list
    public List<InnerTest> list;
    // inner
    public InnerTest inner;
    // You must define default initializer.
    public Test() {
    }

    public Test(Integer i, Double d, String s, Long l, Boolean b, InnerTest inner) {
        this.i = i;
        this.d = d;
        this.s = s;
        this.l = l;
        this.b = b;
        this.list = new ArrayList<>();
        this.inner = inner;
    }
}
```
#### InnerTest
```JAVA
public class InnerTest {

    public Integer i;

    public Double d;

    public String s;

    public Long l;

    public Boolean b;

    // You must define default initializer.
    public InnerTest() {
    }

    public InnerTest(Integer i, Double d, String s, Long l, Boolean b) {
        this.i = i;
        this.d = d;
        this.s = s;
        this.l = l;
        this.b = b;
    }
}
```

#### Main
```JAVA
import com.inursoft.jsonreflection.JSON;

public class Main {

    public static void main(String[] args)
    {
        Test test = new Test(1, 1.0,"s", 1L, false, new InnerTest(1, 1.0, "s", 1L, false));
        test.list.add(new InnerTest(1, 1.0, "s", 1L, false));
        test.list.add(new InnerTest(1, 1.0, "s", 1L, false));
        test.list.add(new InnerTest(1, 1.0, "s", 1L, false));
        // to JSONObject, to Object, to JSONObject
        System.out.println(JSON.toJSONObject(JSON.toObject(Test.class, JSON.toJSONObject(test).toString())).toString());

    }

}
```
#### Result
<pre>
{"b":false,"s":"s","d":1,"i":1,"l":1,"list":[{"b":false,"s":"s","d":1,"i":1,"l":1},{"b":false,"s":"s","d":1,"i":1,"l":1},{"b":false,"s":"s","d":1,"i":1,"l":1}],"inner":{"b":false,"s":"s","d":1,"i":1,"l":1}}
</pre>