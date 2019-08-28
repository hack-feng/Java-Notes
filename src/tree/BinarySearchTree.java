package tree;

/**
 * 二叉搜索树(BST)
 * 动态模拟实现：https://www.cs.usfca.edu/~galles/visualization/BST.html
 * CSDN博客地址：https://blog.csdn.net/qq_34988304/article/details/100102853
 * GitHub地址：https://github.com/hack-feng/algorithm
 * 联系作者：1150640979@qq.com
 */
public class BinarySearchTree {

    // 存放的数据
    private int data;

    // 左节点
    private BinarySearchTree left;

    // 右节点
    private BinarySearchTree right;

    // 查找次数
    private static int searchCount = 0;

    // 私有的构造函数
    private BinarySearchTree(int data){
        this.data = data;
    }

    private BinarySearchTree(BinarySearchTree tree){
        this.data = tree.data;
        this.right = tree.right;
        this.left = tree.left;
    }

    private String toString(BinarySearchTree tree){
        if(tree != null){
            return "{data=" + tree.data + "; left = " + toString(tree.left) + ";right=" +toString(tree.right) + "}";
        }else{
            return "null";
        }
    }

    // 数据插入
    private static void insert(BinarySearchTree node, int data){

        // 如果data的值小于node节点的值，则data作为左节点
        if(node.data > data){
            // 如果node的左节点为空，则将数据直接插入在node的左节点
            if(node.left == null){
                node.left = new BinarySearchTree(data);
            }else{
                // 如果root的左节点不为空，则将root的左节点作为根节点，递归再进行比较
                insert(node.left, data);
            }
        }else{
            // 如果node的右节点为空，则将数据直接插入在node的左节点
            if(node.right == null){
                node.right = new BinarySearchTree(data);
            }else{
                // 如果node的右节点不为空，则将node的右节点作为根节点，递归再进行比较
                insert(node.right, data);
            }
        }
    }

    // 数据遍历
    private static void print(BinarySearchTree root){
        if(root != null){
            // 递归遍历左节点
            print(root.left);
            // 左节点遍历完之后， 依次打印左节点的数据
            System.out.println(root.data);
            // 递归遍历右节点
            print(root.right);
        }
    }

    // 查询数据
    private static BinarySearchTree search(BinarySearchTree root, int data){
        // 查询次数加1
        searchCount ++;
        if(root != null){

            // 如果当前节点数据大于查询数据，递归遍历左节点
            if(root.data > data){
                root = search(root.left, data);
            }else if(root.data < data){
                // 如果当前节点数据小于查询数据，递归遍历右节点
                root = search(root.right, data);
            }else{
                System.out.println("经历" + searchCount + "次，查询到数据" + data);
            }
            return root;
        }else{
            System.out.println("数据不存在");
            return null;
        }
    }

    // 删除data的节点
    private static boolean delete(BinarySearchTree root, int data){
        // true 删除成功   false 不允许删除
        boolean isOk = true;
        // 当前节点的左节点
        BinarySearchTree leftNode = root.left;
        // 当前节点的右节点
        BinarySearchTree rightNode = root.right;

        // 当前节点为需要删除的数据
        if(root.data == data){
            //如果左节点和右节点都为null,则不允许删除
            //分为以下两种情况
            //1：只剩一个根节点，不允许删除
            //2：为叶子节点，直接把父节点对应的节点设为null
            if(leftNode == null && rightNode == null){
                isOk = false;
            }else{

                // 如果左节点为空
                if(leftNode == null){
                    root.data = rightNode.data;
                    root.left = rightNode.left;
                    root.right = rightNode.right;
                }else{
                    // 如果左节点不为空，右节点为空
                    if(leftNode.right == null){
                        root.data = leftNode.data;
                        root.left = leftNode.left;
                        root.right = rightNode;
                    }else{
                        // 如果左节点不为空，右节点也不为空，需要查找左子树最大的节点作为根节点（最右节点）。
                        // 将查询的值赋给当前节点，左子树和右子树不变
                        BinarySearchTree newLeft = findRight(leftNode);
                        root.data = newLeft.data;
                    }
                }
            }
        }else{
            // 当前节点不是需要删除的数据

            // 如果当前节点大于要删除的数据，则遍历左子树查找，否则遍历右子树
            if(root.data > data){
                if(!delete(leftNode, data)){
                    root.left = null;
                    return true;
                }
            }else{
                if(!delete(rightNode, data)){
                    root.right = null;
                    return true;
                }
            }
        }
        return isOk;
    }

    // 只用作删除------>查询当前树的最大节点，返回并移除此节点。
    private static BinarySearchTree findRight(BinarySearchTree node){
        BinarySearchTree rightNode = node.right;
        BinarySearchTree leftNode = node.left;
        // 定义返回的节点
        BinarySearchTree returnNode = new BinarySearchTree(node);

        // 右节点为空，当前节点为最大节点
        if(rightNode == null){
            node.right = null;
            node.right = leftNode;
        }else{
            // 右节点不为空，且右子节点也不为空，则递归查找
            if(rightNode.right != null){
                returnNode = findRight(rightNode);
            }else{
                returnNode = new BinarySearchTree(rightNode);
                // 如果rightNode节点的左节点不为空，移除后，则将其赋给根节点的右节点
                if(rightNode.left != null){
                    node.right = new BinarySearchTree(rightNode.left);
                }
            }
        }
        // 返回最右节点
        return returnNode;
    }


    // 测试用例
    public static void main(String[] args){

        int[] dataArray = {6, 8, 3, 5, 1, 9, 4, 7};
        // 不提供空的构造函数，默认初始化必须赋值。
        // 如果有空的构造函数，把int改为Integer类型，否则会初始化一个data为0的节点
        BinarySearchTree node = new BinarySearchTree(dataArray[0]);

        // 存放数据
        for (int i = 1; i < dataArray.length; i++){
            insert(node, dataArray[i]);
        }

        // 遍历打印数据
        print(node);

        // 查询数据 3
        BinarySearchTree searchNode = search(node, 3);
        if(searchNode != null){
            System.out.println("查询出的数据为" + searchNode.toString(searchNode));
        }

        // 删除节点 6
        delete(node, 6);

        // 遍历删除数据后的数据
        print(node);
    }
}
