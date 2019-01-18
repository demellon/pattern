import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import java.util.ArrayList;
import java.util.Scanner;

enum Color {
    RED, GREEN
}

abstract class Tree {

    private int value;
    private Color color;
    private int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }
}

abstract class TreeVis
{
    public abstract int getResult();
    public abstract void visitNode(TreeNode node);
    public abstract void visitLeaf(TreeLeaf leaf);

}

class SumInLeavesVisitor extends TreeVis {

    int sumLeavesNode = 0;
    public int getResult() {
        return sumLeavesNode;
    }

    public void visitNode(TreeNode node) {
        //implement this
    }

    public void visitLeaf(TreeLeaf leaf) {
        sumLeavesNode = sumLeavesNode + leaf.getValue();
    }
}

class ProductOfRedNodesVisitor extends TreeVis {

    int productRedNodesVisitor = 1;
    public int getResult() {

        return productRedNodesVisitor;
    }

    public void visitNode(TreeNode node) {
        if(node.getColor().equals(Color.RED))
        {
            productRedNodesVisitor = productRedNodesVisitor * node.getValue();
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if(leaf.getColor().equals(Color.RED))
        {
            productRedNodesVisitor = productRedNodesVisitor * leaf.getValue();
        }
    }
}

class FancyVisitor extends TreeVis {
    int x = 0;
    int sunLeafNodesGreen = 0;

    public int getResult() {

        return Math.abs(x - sunLeafNodesGreen);
    }

    public void visitNode(TreeNode node) {
        if(node.getDepth() == 0)
        {
            x = node.getValue();
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if(leaf.getColor().equals(Color.GREEN))
        {
            sunLeafNodesGreen = sunLeafNodesGreen + leaf.getValue();
        }
    }
}

public class Solution {


    private static List<Integer> nodeValues = new ArrayList<>();
    private static List<Color> nodeColors = new ArrayList<>();
    private static HashMap<Integer, Set<Integer>> mapEdges = new HashMap<>();

    public static Tree solve() {

        Scanner scan = new Scanner(System.in);

        int num = scan.nextInt();

        for(int i = 0; i < num; i++)
        {
           nodeValues.add(scan.nextInt());
        }

        for(int i = 0; i < num; i++)
        {
            nodeColors.add(scan.nextInt() == 1 ? Color.GREEN : Color.RED);
        }

        for(int i = 0; i < num -1; i++)
        {
            int x = scan.nextInt();
            int y = scan.nextInt();

            Set<Integer> xRight = mapEdges.get(x);
            if(xRight == null)
            {
                xRight = new HashSet<>();
                mapEdges.put(x, xRight);
            }

            mapEdges.get(x).add(y);

        }

        scan.close();

        //Criando

        TreeNode nodeParent = new TreeNode(nodeValues.get(0), nodeColors.get(0), 0);

        getChildren(1, nodeParent);

        return nodeParent;
    }

    private static void getChildren(Integer nodeNum, TreeNode nodeParent) {
        for (Integer aux : mapEdges.get(nodeNum)) {

            Set<Integer> listChildren = mapEdges.get(aux);
            Tree tree;
            boolean hasChildren = false;
            if (listChildren != null && !listChildren.isEmpty()) {
                hasChildren = true;
                tree = new TreeNode(nodeValues.get(aux - 1), nodeColors.get(aux - 1), nodeParent.getDepth() + 1);
            } else {
                tree = new TreeLeaf(nodeValues.get(aux - 1), nodeColors.get(aux - 1), nodeParent.getDepth() + 1);
            }
            nodeParent.addChild(tree);

            if (hasChildren) {
                getChildren(aux, (TreeNode) tree);
            }

        }
    }


    public static void main(String[] args) {
        Tree root = solve();
        SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
        ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
        FancyVisitor vis3 = new FancyVisitor();

        root.accept(vis1);
        root.accept(vis2);
        root.accept(vis3);

        int res1 = vis1.getResult();
        int res2 = vis2.getResult();
        int res3 = vis3.getResult();

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }
}