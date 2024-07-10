## 背景

先说说问题，问题来源是因为我的开源项目[Maple-Boot](https://gitee.com/hack-feng/maple-boot)项目的网站前端，因为项目主打的内容发布展示，所以其中的内容列表页会根据不同的菜单进行渲染不同的路由。

这里路由`path`使用的是`/blog/:menu?`，通过`menu`的参数来渲染对应的内容，但是遇到了一个问题，在使用`<RouterLink :to="{name: blog, params: {menu:java}}">`跳转时，改变`params`的值，页面不会重新渲染。

~~~js
    {
      path: "/blog/:menu?",
      name: "blog",
      component: BlogView,
    },
~~~

## 官方答疑

查看官网，得到结论如下：

官网地址：[https://router.vuejs.org/zh/guide/essentials/dynamic-matching.html](https://router.vuejs.org/zh/guide/essentials/dynamic-matching.html)

> 使用带有参数的路由时需要注意的是，当用户从 `/users/johnny` 导航到 `/users/jolyne` 时，**相同的组件实例将被重复使用**。因为两个路由都渲染同个组件，比起销毁再创建，复用则显得更加高效。**不过，这也意味着组件的生命周期钩子不会被调用**。

同时也给出了解决方案

**方案一：使用`watch`**

> 要对同一个组件中参数的变化做出响应的话，你可以简单地 watch `$route` 对象上的任意属性，在这个场景中，就是 `$route.params`

**方案二：使用 `beforeRouteUpdate`**

> 或者，使用 `beforeRouteUpdate` [导航守卫](https://router.vuejs.org/zh/guide/advanced/navigation-guards)，它还允许你取消导航

## 我的解决方案

我复用的页面是`BlogView`，原始完整内容如下，主要看不同的内容，防止直接贴部分代码有同学找不到头脑，这里贴全部的内容吧，很多引用是找不到的

~~~vue
<script setup>
import {onMounted, reactive, watch} from "vue";
import { useRoute } from 'vue-router';

import Meta from "@/examples/Meta.vue";
import DefaultNavbar from "@/examples/navbars/NavbarDefault.vue";
import Header from "@/examples/Header.vue";
import DefaultFooter from "@/examples/footers/FooterDefault.vue";
import BlogIndex from "./Sections/BlogIndex.vue";


import {getWebMenuByPath} from "../../api/common";

const route = useRoute();

const state = reactive({
  webMenuInfo: {},
  isGetData: false
});

onMounted(() => {
  getWebMenuByPathClick(route.params.menu);
});

const getWebMenuByPathClick = (menuPath) => {
  getWebMenuByPath(menuPath).then(res => {
    state.webMenuInfo = res;
    state.isGetData = true;
  });
}

</script>
<template>
  <Meta v-if="state.isGetData" :webMenuInfo="state.webMenuInfo"/>
  <div class="container position-sticky z-index-sticky top-0  opacity-8">
    <div class="row">
      <div class="col-12">
        <DefaultNavbar :sticky="true"/>
      </div>
    </div>
  </div>
  <Header>
    <div
      class="page-header min-height-400"
      :style="{ backgroundImage: `url(${state.webMenuInfo.image})` }"
      loading="lazy"
    >
      <span class="mask bg-gradient-dark opacity-3"></span>
    </div>
  </Header>
  <BlogIndex :menuPath="state.webMenuInfo.path"/>
  <DefaultFooter />
</template>
~~~

修改后的内容

~~~vue
<script setup>
import {onMounted, reactive, watch} from "vue";
import { useRoute } from 'vue-router';

import Meta from "@/examples/Meta.vue";
import DefaultNavbar from "@/examples/navbars/NavbarDefault.vue";
import Header from "@/examples/Header.vue";
import DefaultFooter from "@/examples/footers/FooterDefault.vue";
import BlogIndex from "./Sections/BlogIndex.vue";


import {getWebMenuByPath} from "../../api/common";

const route = useRoute();

const state = reactive({
  webMenuInfo: {},
  isGetData: false
});

onMounted(() => {
  getWebMenuByPathClick(route.params.menu);
});

const getWebMenuByPathClick = (menuPath) => {
  getWebMenuByPath(menuPath).then(res => {
    state.webMenuInfo = res;
    state.isGetData = true;
  });
}

watch(() => route.params.menu, (newId, oldId) => {
  getWebMenuByPathClick(route.params.menu);
})

</script>
<template>
  <Meta v-if="state.isGetData" :webMenuInfo="state.webMenuInfo"/>
  <div class="container position-sticky z-index-sticky top-0  opacity-8">
    <div class="row">
      <div class="col-12">
        <DefaultNavbar :sticky="true"/>
      </div>
    </div>
  </div>
  <Header>
    <div
      class="page-header min-height-400"
      :style="{ backgroundImage: `url(${state.webMenuInfo.image})` }"
      loading="lazy"
    >
      <span class="mask bg-gradient-dark opacity-3"></span>
    </div>
  </Header>
  <BlogIndex :menuPath="state.webMenuInfo.path" :key="state.webMenuInfo.path"/>
  <DefaultFooter />
</template>
~~~

**变更点一**：变更的点主要是加了`watch`监听`route.params`变化时，重新请求数据。

~~~js
watch(() => route.params.menu, (newId, oldId) => {
  getWebMenuByPathClick(route.params.menu);
})
~~~

**变更点二**：在`<BlogIndex>`子组件上添加`:key="state.webMenuInfo.path"`，通过不同的key标注为不同组件

~~~
<BlogIndex :menuPath="state.webMenuInfo.path" :key="state.webMenuInfo.path"/>
~~~

## 看下效果

通过路由`/blog/article`可以看到背景图和分类的数据查询出来了

![image-20240709102823264](https://image.xiaoxiaofeng.site/blog/2024/07/09/xxf-20240709102823.png?xxfjava)

当路由切换到`/blog/nterview-fenbushi`，可以看到背景图发生了变化，同时因为没有配置对应的分类栏目，数据渲染为空的。

![image-20240709102850515](https://image.xiaoxiaofeng.site/blog/2024/07/09/xxf-20240709102851.png?xxfjava)
