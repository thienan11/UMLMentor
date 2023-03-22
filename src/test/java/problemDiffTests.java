
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class problemDiffTests {

    private Assertions Assert;

    @Test
    public void testEmptyCorrectSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testEmptyIncorrectExtraSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(null);

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testOneClassEmptyCorrectSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testOneClassEmptyIncorrectAbsentSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Absent");
        expected.get(1).add(null);

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testOneClassEmptyIncorrectExtraSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        student.put("B", new UserClass("B"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(null);

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testOneClassEmptyCorrectNameSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("B", new UserClass("B"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testOneClassEmptyCorrectUnneededMethodSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass A = new UserClass("A");
        A.makeMethod("meth");
        student.put("A", new UserClass("A"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testTwoClassesEmptyCorrectSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        student.put("Z", new UserClass("Z"));
        solution.put("A", new UserClass("A"));
        solution.put("G", new UserClass("G"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testTwoClassesConnectedCorrectSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass ASol = new UserClass("A");
        UserClass Z = new UserClass("Z");
        UserClass G = new UserClass("G");
        AStu.addGlobal("Var", Z, 4);
        ASol.addGlobal("Var", G, 4);
        student.put("A", AStu);
        student.put("Z", Z);
        solution.put("A", ASol);
        solution.put("G", G);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testTwoClassesConnectedIncorrectSpecZero(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass ASol = new UserClass("A");
        UserClass Z = new UserClass("Z");
        UserClass G = new UserClass("G");
        ASol.addGlobal("Var", G, 4);
        student.put("A", AStu);
        student.put("Z", Z);
        solution.put("A", ASol);
        solution.put("G", G);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Missing Connection " + 4 + " Z");
        expected.get(1).add(AStu);

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testTwoClassesMultiConnectedCorrectSpecZeroA(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass ASol = new UserClass("A");
        UserClass Z = new UserClass("Z");
        UserClass G = new UserClass("G");
        AStu.addGlobal("Var", Z, 4);
        AStu.setParent(Z);
        ASol.addGlobal("Var", G, 4);
        ASol.setParent(G);
        student.put("A", AStu);
        student.put("Z", Z);
        solution.put("A", ASol);
        solution.put("G", G);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testTwoClassesMultiConnectedCorrectSpecZeroB(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass ASol = new UserClass("A");
        UserClass Z = new UserClass("Z");
        UserClass G = new UserClass("G");
        AStu.makeMethod("Meth");
        AStu.makeMethodVar("Meth", "var", Z, 4);
        Z.makeMethod("Meth");
        Z.makeMethodVar("Meth", "var", AStu, 4);
        ASol.makeMethod("Meth");
        ASol.makeMethodVar("Meth", "var", G, 4);
        G.makeMethod("Meth");
        G.makeMethodVar("Meth", "var", ASol, 4);
        student.put("A", AStu);
        student.put("Z", Z);
        solution.put("A", ASol);
        solution.put("G", G);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testTwoClassesMultiConnectedIncorrectSpecZeroA(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass ASol = new UserClass("A");
        UserClass Z = new UserClass("Z");
        UserClass G = new UserClass("G");
        AStu.addGlobal("Var", Z, 4);
        AStu.setParent(Z);
        ASol.addGlobal("Var", G, 4);
        student.put("A", AStu);
        student.put("Z", Z);
        solution.put("A", ASol);
        solution.put("G", G);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Unneeded Connection " + 1 + " Z");
        expected.get(1).add(AStu);

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testTwoClassesMultiConnectedIncorrectSpecZeroB(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass ASol = new UserClass("A");
        UserClass Z = new UserClass("Z");
        UserClass G = new UserClass("G");
        AStu.makeMethod("Meth");
        AStu.makeMethodVar("Meth", "var", Z, 4);
        ASol.makeMethod("Meth");
        ASol.makeMethodVar("Meth", "var", G, 4);
        G.makeMethod("Meth");
        G.makeMethodVar("Meth", "var", ASol, 4);
        student.put("A", AStu);
        student.put("Z", Z);
        solution.put("A", ASol);
        solution.put("G", G);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Missing Connection 4 A");
        expected.get(1).add(Z);

        Assert.assertEquals(expected, p.diff(0));
    }

    @Test
    public void testEmptyCorrectSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testEmptyExtraSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testOneClassEmptyCorrectSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testOneClassEmptyIncorrectAbsentSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Absent");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testOneClassEmptyIncorrectExtraSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        student.put("B", new UserClass("B"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(new UserClass("B"));

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testOneClassEmptyIncorrectNameSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("B", new UserClass("B"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(new UserClass("B"));
        expected.get(0).add("Absent");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testOneClassEmptyIncorrectUnneededGlobalSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass A = new UserClass("A");
        A.addGlobal("Var", new UserClass("B"), 4);
        student.put("A", A);
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Unneeded Connection 4 B");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testOneClassEmptyCorrectUnneededMethodSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass A = new UserClass("A");
        A.makeMethod("meth");
        student.put("A", A);
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testTwoClassConnectedCorrectSpecOne(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass A = new UserClass("A");
        UserClass B = new UserClass("B");
        A.addGlobal("var", B, 4);
        student.put("A", A);
        student.put("B", B);
        solution.put("A", A);
        solution.put("B", B);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testTwoClassConnectedIncorrectSpecOneA(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass BStu = new UserClass("B");
        BStu.addGlobal("var", AStu, 4);
        UserClass ASol = new UserClass("A");
        UserClass BSol = new UserClass("B");
        ASol.addGlobal("var", BSol, 4);
        student.put("A", AStu);
        student.put("B", BStu);
        solution.put("A", ASol);
        solution.put("B", BSol);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Missing Connection 4 B");
        expected.get(1).add(AStu);
        expected.get(0).add("Unneeded Connection 4 A");
        expected.get(1).add(BStu);

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testTwoClassConnectedIncorrectSpecOneB(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass BStu = new UserClass("B");
        AStu.setParent(BStu);
        BStu.addGlobal("var", AStu, 4);
        UserClass ASol = new UserClass("A");
        UserClass BSol = new UserClass("B");
        ASol.addGlobal("var", BSol, 4);
        BSol.setParent(ASol);
        student.put("A", AStu);
        student.put("B", BStu);
        solution.put("A", ASol);
        solution.put("B", BSol);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Unneeded Connection 1 B");
        expected.get(1).add(AStu);
        expected.get(0).add("Missing Connection 4 B");
        expected.get(1).add(AStu);
        expected.get(0).add("Unneeded Connection 4 A");
        expected.get(1).add(BStu);
        expected.get(0).add("Missing Connection 1 A");
        expected.get(1).add(BStu);

        Assert.assertEquals(expected, p.diff(1));
    }

    @Test
    public void testEmptyCorrectSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testEmptyExtraSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testOneClassEmptyCorrectSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testOneClassEmptyIncorrectAbsentSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Absent");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testOneClassEmptyIncorrectExtraSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("A", new UserClass("A"));
        student.put("B", new UserClass("B"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(new UserClass("B"));

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testOneClassEmptyIncorrectNameSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        student.put("B", new UserClass("B"));
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Extra");
        expected.get(1).add(new UserClass("B"));
        expected.get(0).add("Absent");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testOneClassEmptyIncorrectUnneededGlobalSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass A = new UserClass("A");
        A.addGlobal("Var", new UserClass("B"), 4);
        student.put("A", A);
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Unneeded Item variable Var");
        expected.get(1).add(new UserClass("A"));

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testOneClassEmptyIncorrectUnneededMethodSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass A = new UserClass("A");
        A.makeMethod("meth");
        student.put("A", A);
        solution.put("A", new UserClass("A"));
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Unneeded Item method meth");
        expected.get(1).add(A);

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testTwoClassConnectedCorrectSpecThree(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass A = new UserClass("A");
        UserClass B = new UserClass("B");
        A.addGlobal("var", B, 4);
        student.put("A", A);
        student.put("B", B);
        solution.put("A", A);
        solution.put("B", B);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testTwoClassConnectedIncorrectSpecThreeA(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass BStu = new UserClass("B");
        BStu.addGlobal("var", AStu, 4);
        UserClass ASol = new UserClass("A");
        UserClass BSol = new UserClass("B");
        ASol.addGlobal("var", BSol, 4);
        student.put("A", AStu);
        student.put("B", BStu);
        solution.put("A", ASol);
        solution.put("B", BSol);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Missing Item variable var");
        expected.get(1).add(AStu);
        expected.get(0).add("Unneeded Item variable var");
        expected.get(1).add(BStu);

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testTwoClassConnectedIncorrectSpecThreeB(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass BStu = new UserClass("B");
        AStu.setParent(BStu);
        BStu.addGlobal("var", AStu, 4);
        UserClass ASol = new UserClass("A");
        UserClass BSol = new UserClass("B");
        ASol.addGlobal("var", BSol, 4);
        BSol.setParent(ASol);
        student.put("A", AStu);
        student.put("B", BStu);
        solution.put("A", ASol);
        solution.put("B", BSol);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Unneeded Connection 1 B");
        expected.get(1).add(AStu);
        expected.get(0).add("Missing Item variable var");
        expected.get(1).add(AStu);
        expected.get(0).add("Missing Connection 1 A");
        expected.get(1).add(BStu);
        expected.get(0).add("Unneeded Item variable var");
        expected.get(1).add(BStu);

        Assert.assertEquals(expected, p.diff(3));
    }

    @Test
    public void testTwoClassConnectedIncorrectSpecThreeC(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap student = new HashMap();
        HashMap solution = new HashMap();
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        UserClass AStu = new UserClass("A");
        UserClass BStu = new UserClass("B");
        AStu.makeMethod("me");
        AStu.makeMethod("meth");
        AStu.makeMethodVar("meth", "var", BStu, 0);
        UserClass ASol = new UserClass("A");
        UserClass BSol = new UserClass("B");
        ASol.makeMethod("me");
        ASol.makeMethod("meth");
        ASol.makeMethodVar("me", "var", BStu, 0);
        student.put("A", AStu);
        student.put("B", BStu);
        solution.put("A", ASol);
        solution.put("B", BSol);
        blackboard.setClasses(student);
        blackboard.setProblemSolution(solution);

        ArrayList<ArrayList<Object>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        expected.get(0).add("Modify Item variable var");
        expected.get(1).add(AStu);

        Assert.assertEquals(expected, p.diff(3));
    }
}
