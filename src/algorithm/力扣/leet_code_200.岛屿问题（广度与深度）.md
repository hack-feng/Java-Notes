### 题目信息 
给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。

岛屿总是被水包围，并且每座岛屿只能由水平方向或竖直方向上相邻的陆地连接形成。

此外，你可以假设该网格的四条边均被水包围。

示例 1:
~~~
输入:
[
['1','1','1','1','0'],
['1','1','0','1','0'],
['1','1','0','0','0'],
['0','0','0','0','0']
]
输出: 1
~~~

示例 2:
~~~
输入:
[
['1','1','0','0','0'],
['1','1','0','0','0'],
['0','0','1','0','0'],
['0','0','0','1','1']
]
输出: 3
~~~
> 解释: 每座岛屿只能由水平和/或竖直方向上相邻的陆地连接而成。

### 代码实现
~~~java
import java.util.LinkedList;
import java.util.Queue;

public class Test {
    public static void main(String[] args) {
        // 解决岛屿问题
        int[][] land = new int [][]{
                {1,1,0,1,0,0,0,1},
                {0,0,1,0,1,1,1,1},
                {1,1,0,1,1,1,0,0},
                {1,1,0,1,1,1,0,0}
        };
        // System.out.println("深度优先查询：" + getDfsNum(land));
        System.out.println("广度优先查询：" + bfs(land));
    }

    /**
     * 深度优先查询思想：
     * 出现第一个1时，则最少有一个岛屿，然后查其上下左右的值，如果出现为1的，则递归深度查询出所有紧邻的岛屿，出现1，就继续找，不是1，则退出。
     * 遍历到岛屿为1时，将其设为0，因为连着都属于同一个岛屿。故只需在第一个数字上加1.
     * 根据第一个值，一直往下找，直到找到最底层后，然后再处理分支上的数据
     */
    private static int getDfsNum(int[][] grid){
        int landNum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] == 1){
                    landNum ++;
                    dfs(grid, i, j);
                }
            }
        }
        return landNum;
    }

    private static void dfs(int[][] grid, int i, int j){
        // 如果超出边界或者为0，则直接退出，这里我们只找为1的
        if(i >= grid.length || i < 0 || j >= grid[0].length || j < 0 || grid[i][j] == 0){
            return ;
        }
        // 如果为1，则与上个岛屿连接，均可视为海水，不加数量，故设为0
        grid[i][j] = 0;
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }


    /**
     * 广度优先搜索思想：
     * 出现第一个1时，则最少有一个岛屿，然后查其上下左右的值，如果出现为1的，则将该位置的数据放入队列中，出现1，就继续找，不是1，则退出。
     * 遍历到岛屿为1时，将其设为0，因为连着都属于同一个岛屿。故只需在第一个数字上加1.
     * 根据第一个值，一层层的往下找，利用队列先进先出的原则，直到该层全部处理完后，再处理下一层。
     */
    private static int bfs(int[][] grid){
        if(grid == null || grid.length == 0){
            return 0;
        }
        int landNum = 0;
        for(int i = 0; i < grid.length; i ++){
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] == 1){
                    // 出现第一个岛屿，岛屿数直接+1
                    landNum ++;
                    // 已加完数量，将岛屿设为0，与海水混为一谈
                    grid[i][j] = 0;
                    Queue<Integer> queue = new LinkedList<>();
                    queue.add(i * grid[0].length + j);
                    // 队列不为空，则一直处理下去
                    while(!queue.isEmpty()){
                        // 遍历到当前，没有价值，删掉当前值
                        int id = queue.remove();
                        // 除数求第几行
                        int row = id / grid[0].length;
                        // 余数求第几列
                        int col = id % grid[0].length;

                        // 如果为1，则与上个岛屿连接，均可视为海水，不加数量，故设为0
                        // 上
                        if(row - 1 >= 0 && grid[row - 1][col] == 1){
                            grid[row-1][col] = 0;
                            queue.offer((row - 1) * grid[0].length + col);
                        }

                        // 下
                        if(row + 1 < grid.length && grid[row + 1][col] == 1){
                            grid[row+1][col] = 0;
                            queue.offer((row + 1) * grid[0].length + col);
                        }

                        // 左
                        if(col - 1 > 0 && grid[row][col - 1] == 1){
                            grid[row][col-1] = 0;
                            queue.offer(row * grid[0].length + (col - 1));
                        }

                        // 右
                        if(col + 1 < grid[0].length && grid[row][col + 1] == 1){
                            grid[row][col+1] = 0;
                            queue.offer(row * grid[0].length + (col + 1));
                        }
                    }
                }
            }
        }
        return landNum;
    }
}
~~~