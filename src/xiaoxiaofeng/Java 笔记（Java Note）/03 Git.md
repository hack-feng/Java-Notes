# SVN #

 *  SVN（Subversion），集中式版本控制系统，CVS（Concurrent Versions System）的替代产物
 *  下载仓库资源 checkout、修改/提交 commit 仓库资源、同步 update 仓库资源

# Git #

 *  分布式版本控制系统
 *  [Pro Git][]

## Git 常用命令 ##

### 用户配置 ###

 *  git config —global user.name "Your Name"：全局配置用户名
 *  git config —global user.email "email@example.com"：全局配置邮箱
 *  git config —global alias.plm 'pull origin master'：设置别名
 *  配置级别
    
     *  —local：默认，高优先级，只影响本仓库，保存目录 .git/config
     *  —global：中优先级，影响到所有当前用户的git仓库，保存目录 ~/.gitconfig
     *  — system：低优先级，影响到全系统的git仓库，保存目录 /etc/gitconfig

### 基本操作 ###

 *  git init：在当前目录下初始化仓库（—bare，纯版本库，没有工作目录）
 *  git add readme.txt：把文件添加到暂存区（同时文件被跟踪）
 *  git commit -m "wrote a readme file"：提交到仓库
 *  git log —pretty=oneline：查看提交日志
 *  git reflog：查看提交日志（包括已经撤销的提交，即 HEAD 移动的记录）
 *  git log —color —graph —pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' —abbrev-commit —date=relative
 *  git status：查看当前版本库的状态
 *  git diff readme.txt：查看差异
 *  git checkout — readme.txt：撤销 readme.txt 文件在工作区的全部修改（对比暂存区或版本库）
 *  git reset —hard HEAD^：回退到上一个版本
 *  git reset —hard  ：回退到指定版本
 *  git revert HEAD：撤销最近一次提交
 *  git revert HEAD~1：撤销上上次的提交，注意：数字从 0 开始
 *  git revert  ：撤销指定的提交，撤销也会作为一次提交进行保存
 *  git revert 是用一次新的 commit 来回滚之前的 commit，git reset 是直接删除指定的 commit
 *  git rm —cached log/\*.log：已经 push 过的文件，想从 git 远程库中删除，并在以后的提交中忽略，但在本地保留该文件
 *  git update-index —assume-unchanged config/config.xml：已经 push 过的文件，想在以后的提交时忽略此文件，即使本地已经修改过，且不删除 git 远程库中相应文件
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414643628.png) 
    图 1 git-status

### 分支操作 ###

 *  git branch：查看本地分支列表
 *  git branch -r：查看远程分支列表
 *  git branch  ：创建分支
 *  git branch -d  ：删除分支
 *  git branch -D  ：强制删除分支
 *  git branch -u origin/ ：设置本地分支跟踪的远程分支（上游分支）或 git branch —set-upstream-to origin/
 *  git branch —unset-upstream：取消跟踪远程分支（上游分支）
 *  git checkout  ：切换分支（切换分支前要先提交改动或使用 git stash 保存当前的操作）
 *  git checkout -b  \-t origin/  ：创建 + 切换分支，（-t 设置跟踪的远程分支，可省略）
 *  git merge  ：合并某分支的所有变更到当前分支
 *  git cherry-pick  ,  ：获得在某个提交中引入的变更，然后尝试将作为一个新的提交引入到 **当前分支上**
    

### 存储操作 ###

 *  git stash save some\_msg：保存当前分支所有没有提交的操作
 *  git stash list：查看保存的操作
 *  git stash pop stash@\{num\}：恢复某一操作，并把这条 stash 记录删除
 *  git stash clear：清空保存的所有操作

### 标签 ###

 *  git tag：列出已有的标签
 *  git tag v1.0：基于最新提交创建标签
 *  git tag -a v1.2 9fceb02：对指定的提交创建标签
 *  git checkout v1.0：切换到某个 tag

### 远程操作 ###

 *  创建 SSH Key：`ssh-keygen -t rsa -C "youremail@example.com"`
 *  在 GitLab 上添加的 SSH Key
 *  添加远程仓库并命名为 origin：`git remote add origin git@192.168.113.47:sdky/learngit.git`（可添加多个），或者克隆远程仓库作为本地仓库：`git clone git@192.168.113.47:sdky/learngit.git`
 *  获取远程仓库的提交历史：`git fetch origin`
 *  把远程仓库最新的内容更新到本地仓库：`git pull origin master`（git pull = git fetch + git merge）
 *  把本地仓库的 master 分支内容推送到远程仓库 origin 的 master 分支（**-u 关联两个分支**）： `git push -u origin master` 或者 `git push origin master` 或者 `git push origin master:master`push.default 在 Git 2.0 版本后的默认值为 simple，即：在中央仓库工作流程模式下，拒绝推送到上游与本地分支名字不同的分支，也就是只有本地分支名和上游分支名字一致才可以推送，且只会推送本地当前分支
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414644593.png) 
    图 2 Git常用命令

## 开发方式 ##

 *  分支开发，主干发布：需要开发一个新功能或者修复一个 Bug 时，从主干拉一个分支进行开发，开发完成且测试通过后，合并回主干，然后从主干进行发布
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414646890.png) 
    图 3 分支模型


[Pro Git]: https://git-scm.com/book/zh/v2
[git-status]: https://static.sitestack.cn/projects/sdky-java-note/32bf334e455e427fc576f1dda9113f2a.png
[Git]: https://static.sitestack.cn/projects/sdky-java-note/924bc8b8e73d0fda4fa7cc69f46c1daf.png
[d478fae6b11e643322b6470770b0282c.png]: https://static.sitestack.cn/projects/sdky-java-note/d478fae6b11e643322b6470770b0282c.png