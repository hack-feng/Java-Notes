package algorithm.tree;

/**
 * 平衡二叉树（AVL树）
 * 这里只模拟int类型实现，如果需要其他类型，请将int类型修改为泛型，并实现extends Comparable<T>接口，方便比较
 * 动态模拟实现：https://www.cs.usfca.edu/~galles/visualization/AVLtree.html
 * GitHub地址：https://github.com/hack-feng/algorithm
 * CSDN博客地址：https://blog.csdn.net/qq_34988304/article/details/100130571
 * 联系作者：1150640979@qq.com
 */
public class AVLTree{

    private Node root;

    // 定义树的节点
    static class Node{
        int data;

        Node left;

        Node right;

        // 树的高度
        int height;

        Node(Node left, Node right, int data) {
            this.left = left;
            this.right = right;
            this.data = data;
            this.height = 1;
        }
    }


    // 插入节点
    private void insert(int data){
        root = insert(data, root);
    }

    private Node insert(int data, Node node){
        if(node == null){
            Node returnNode =  new Node(null, null, data);
            // 新创建的树，高度默认为1
            returnNode.height = 1;
            return returnNode;
        }else{
            // 如果root.data的值大于data, 则在左子树上插入，否则在右子树上插入
            if(node.data > data){
                node.left = insert(data, node.left);
                // 判断树是否失衡，失衡需要旋转
                if(getHeight(node.left) > getHeight(node.right) + 1){

                    // 判断进行两次旋转还是一次旋转
                    if(node.left.data > data){
                        //（LL）一次旋转， 右旋
                        node = LL(node);
                    }else{
                        //（LR）两次旋转， 先左旋，再右旋
                        node = LR(node);
                    }
                }
            }else{
                node.right = insert(data, node.right);
                // 判断树是否失衡，失衡需要旋转
                if(getHeight(node.right) > getHeight(node.left) + 1){
                    // 判断进行两次旋转还是一次旋转
                    if(node.right.data > data){
                        //（RL）两次旋转,先右旋，再左旋
                        node = RL(node);
                    }else{
                        //（RR）一次旋转,左旋
                        node = RR(node);
                    }
                }
            }
            // 重新计算树的高度
            node.height = setHeight(node);
            return node;
        }
    }

    // 左左情况， 进行一次右旋
    private Node LL(Node node){
        Node leftNode = node.left;
        node.left = leftNode.right;
        leftNode.right = node;

        //重新计算高度
        node.height = setHeight(node);
        leftNode.height = setHeight(leftNode);
        return leftNode;
    }

    // 左右情况， 先进行左旋，再进行一次右旋
    private Node LR(Node node){
        node.left = RR(node.left);
        node = LL(node);
        return node;
    }

    // 右右情况， 进行一次左旋
    private Node RR(Node node){
        Node rightNode = node.right;
        node.right = rightNode.left;
        rightNode.left = node;

        //重新计算高度
        node.height = setHeight(node);
        rightNode.height = setHeight(rightNode);
        return rightNode;
    }

    // 右左情况，先进行右旋，再进行一次左旋
    private Node RL(Node node){
        node.right = LL(node.right);
        node = RR(node);
        return node;
    }

    // 获取树的高度
    private int getHeight(Node node){
        return node == null ? 0 : node.height;
    }

    // 重新计算树的高度
    private int setHeight(Node node){
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    // 打印节点
    private void printNode(Node root){
        if(root != null){
            printNode(root.left);
            System.out.println(root.data);
            printNode(root.right);
        }
    }

    public static void main(String [] args){
//        int[] dataArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] dataArray = new int[]{2, 8, 5, 6, 7, 4, 3, 1, 9};
        AVLTree tree = new AVLTree();
        for (int i : dataArray) {
            tree.insert(i);
        }
        tree.printNode(tree.root);
    }
}
