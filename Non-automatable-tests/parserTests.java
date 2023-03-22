import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class parserTests {

    @Test
    public void testEmpty(){
        String s = "";

        HashMap<String, UserClass> expected = new HashMap<>();

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testOneEmptyClass1(){
        String s = "class a{}";

        HashMap<String, UserClass> expected = new HashMap<>();
        expected.put("a", new UserClass("a"));

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testOneEmptyClass2(){
        String s = "class           a       {\n\n\n          }";

        HashMap<String, UserClass> expected = new HashMap<>();
        expected.put("a", new UserClass("a"));

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testOneEmptyClassError(){
        String s = "class a{";
        Assertions.assertEquals(null, new Parser().parse(s));
    }

    @Test
    public void testOneClass1(){
        String s = "class a{\nvoid method(){}}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        a.makeMethod("method");
        expected.put("a", a);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testOneClassError1(){
        String s = "class a{\nmethod(){}}";
        Assertions.assertEquals(null, new Parser().parse(s));
    }

    @Test
    public void testOneClassError2(){
        String s = "class a{\nmethod()}}";
        Assertions.assertEquals(null, new Parser().parse(s));
    }

    @Test
    public void testOneClassError3(){
        String s = "class a{\nmethod({}}";
        Assertions.assertEquals(null, new Parser().parse(s));
    }

    @Test
    public void testTwoClassUnconnected1(){
        String s = "class a{}\nclass b{}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        UserClass b = new UserClass("b");
        expected.put("a", a);
        expected.put("b", b);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testTwoClassUnconnected2(){
        String s = "class a{}\nclass b{void method(){}}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        UserClass b = new UserClass("b");
        b.makeMethod("method");
        expected.put("a", a);
        expected.put("b", b);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testTwoClassConnected1(){
        String s = "class a{}\nclass b{void method (){a name;}}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        UserClass b = new UserClass("b");
        b.makeMethod("method");
        b.makeMethodVar("method", "name", a, 0);
        expected.put("a", a);
        expected.put("b", b);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testTwoClassConnected2(){
        String s = "class a{b name;}\nclass b{}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        UserClass b = new UserClass("b");
        a.addGlobal("name", b, 4);
        expected.put("a", a);
        expected.put("b", b);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testTwoClassConnected3(){
        String s = "class a extends b{}\nclass b{}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        UserClass b = new UserClass("b");
        a.setParent(b);
        expected.put("a", a);
        expected.put("b", b);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testTwoClassConnected4(){
        String s = "class a{void method(b name){}}\nclass b{a var;}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        UserClass b = new UserClass("b");
        a.makeMethod("method");
        a.makeMethodVar("method", "name", b, 2);
        b.addGlobal("var", a, 4);
        expected.put("a", a);
        expected.put("b", b);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testTwoClassConnected5(){
        String s = "class a{void method(b name){}}";

        HashMap<String, UserClass> expected = new HashMap<>();
        UserClass a = new UserClass("a");
        UserClass b = new UserClass("b");
        a.makeMethod("method");
        a.makeMethodVar("method", "name", b, 2);
        expected.put("a", a);
        expected.put("b", b);

        Assertions.assertEquals(expected, new Parser().parse(s));
    }

    @Test
    public void testTwoClassConnectedError1(){
        String s = "class a{void method(b){}}";
        Assertions.assertEquals(null, new Parser().parse(s));
    }
    @Test
    public void testTwoClassConnectedError2(){
        String s = "class a{void method(){b}} class b{}";
        Assertions.assertEquals(null, new Parser().parse(s));
    }

    @Test
    public void testTwoClassConnectedError3(){
        String s = "class a{b} class b{}";
        Assertions.assertEquals(null, new Parser().parse(s));
    }


    //test connections errors and wacky spacing and classes undeclared but used in the code
}
