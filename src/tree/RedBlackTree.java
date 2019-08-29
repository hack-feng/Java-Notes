package tree;

/**
 * 红黑树（R-B Tree）
 * 这里只模拟int类型实现，如果需要其他类型，请将int类型修改为泛型，并实现extends Comparable<T>接口，方便比较compareTo
 * 动态模拟实现：https://www.cs.usfca.edu/~galles/visualization/RedBlack.html
 * GitHub地址：https://github.com/hack-feng/algorithm
 * 理论知识：https://blog.csdn.net/qq_34988304/article/details/100135759
 * CSDN博客地址：
 * 联系作者：1150640979@qq.com
 *
 * 红黑树的特性:
 * （1）每个节点或者是黑色，或者是红色。
 * （2）根节点是黑色。
 * （3）每个叶子节点（NIL）是黑色。 [注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！]
 * （4）如果一个节点是红色的，则它的子节点必须是黑色的。
 * （5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。
 */
public class RedBlackTree {

    private RBNode root;    // 根结点

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    static class RBNode{
        // 节点颜色， 红：false   黑：true
        boolean color;
        int data;
        RBNode left;
        RBNode right;
        RBNode parent;

        RBNode(int data, RBNode left, RBNode right){
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    private void insert(int data){
        RBNode node = new RBNode(data, null, null);
        insert(node);
    }

    private void insert(RBNode node){
        // 定义node的根节点
        RBNode pNode = null;
        // 定义一个临时节点
        RBNode tempNode = this.root;

        // 取出root的根节点
        while(tempNode != null){
            pNode = tempNode;
            if(tempNode.data > node.data){
                tempNode = node.left;
            }else{
                tempNode = node.right;
            }
        }

        // 设置node的父节点
        node.parent = pNode;

        // 判断将node放在其父节点的左节点还是右节点
        if(pNode != null){
            if(pNode.data > node.data){
                pNode.left = node;
            }else{
                pNode.right = node;
            }
        }else{
            this.root = node;
        }

        // 初始化节点的颜色为红色
        node.color = RED;

        // 修正为红黑树
        insertFixUp(node);
    }

    /**
     * 修正红黑树
     * 分为一下几种情况:
     * 1:如果是父节点，直接变成黑色
     * 2:如果父节点是黑色，不违背特性，直接添加即可
     * 3:如果父节点是红色，添加违背特性。则又分为一下几种情况：
     *  3.1:父节点是祖父节点的左节点
     *      3.1.1:如果叔叔节点为黑的时候
     *          3.1.1.1:如果插入的节点是父节点的左节点
     *          3.1.1.2:如果插入的节点是父节点的右节点
     *      3.1.2:如果叔叔节点为红的时候
     *          直接将父节点和叔叔节点涂黑，祖父节点涂红就可以了
     *  3.2:父节点是祖父节点的右节点
     *      3.2.1:如果叔叔节点为黑的时候
     *          3.2.1.1:如果插入的节点是父节点的左节点
     *          3.2.1.2:如果插入的节点是父节点的右节点
     *      3.2.2:如果叔叔节点为红的时候
     *          直接将父节点和叔叔节点涂黑，祖父节点涂红就可以了
     */
    private void insertFixUp(RBNode node) {
        RBNode pNode, gNode;

        pNode = node.parent;

        // 如果父节点不为空，并且父节点的颜色是红色，则会触发树旋转
        while(pNode != null && isRed(pNode.color)){
            gNode = pNode.parent;

            // 父节点为祖父节点的左节点时
            if(gNode != null && gNode.left == pNode){
                RBNode uNode = gNode.right;
                //叔叔节点为黑色
                if(uNode.color){

                }else{
                    pNode.color = BLACK;
                    uNode.color = BLACK;
                    gNode.color = RED;
                    node = gNode;
                    continue;
                }
            }else{
                RBNode uNode = gNode.left;
                //叔叔节点为黑色
                if(uNode.color){

                }else{
                    pNode.color = BLACK;
                    uNode.color = BLACK;
                    gNode.color = RED;
                    node = gNode;
                    continue;
                }
            }

        }

        // 将根节点设为黑色
        this.root.color = BLACK;
    }

    public boolean isRed(boolean color){
        return !color;
    }

    public static void main(String[] args){
        RedBlackTree rbTree = new RedBlackTree();
        int[] dataArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i : dataArray) {
            rbTree.insert(i);
        }
    }

}
