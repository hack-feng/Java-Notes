## 1. 接入准备

1. 在同意与偏好管理平台创建对应的采集点和目的。

2. 保存好创建好的采集点ID（CP开始的一串编码，例：CP20240201155280）



## 2. 接口授权

### 2.1 调用获取采集点的目的列表的接口（/auth/getPurposeListByPointNo）

* 使用描述：在需要采集的地方调用接口，根据采集点ID获取对应的目的数据。展示到用户界面，采集数据。

* 接口地址：https://cpm-uat.cangoelectronics.com/auth/getPurposeListByPointNo
* 接口详情见【附1. 接口名称：获取采集点的目的列表】



### 2.2 调用用户授权采集接口（/auth/accountAuth）

* 使用描述：对采集到的数据，调用接口保存到数据库中。

* 接口地址：https://cpm-uat.cangoelectronics.com/auth/accountAuth

* 接口详情见【附2. 接口名称：用户授权采集】



## 附：接口信息

#### 附1. 接口名称：获取采集点的目的列表

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th><th key=explain>说明</th>
    </tr>
  </thead>
  <tbody className="ant-table-tbody">
                <tr> <td>请求地址</td> <td>/auth/getPurposeListByPointNo</td> </tr>
                <tr> <td>请求类型</td> <td>POST</td> </tr>
              </tbody>
              </table>


##### 请求参数

<h6>Headers</h6>

| 参数名称     | 参数值           | 必须 | 示例 | 备注 |
| ------------ | ---------------- | ---- | ---- | ---- |
| Content-Type | application/json | Y    |      |      |


<h6>Body</h6>

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>参数</th><th key=desc>名称</th><th key=type>类型</th><th key=required>必须</th><th key=sub>长度</th><th key=desc>说明</th>
    </tr>
  </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> pointNo</span></td><td key=1><span style="white-space: pre-wrap">采集点编码</span></td><td key=2><span>string</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> subjectData</span></td><td key=1><span style="white-space: pre-wrap">数据主体信息</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr>
               </tbody>
              </table>


##### 响应参数

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>参数</th><th key=desc>名称</th><th key=type>类型</th><th key=required>必须</th><th key=sub>长度</th><th key=desc>说明</th>
    </tr>
  </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> msg</span></td><td key=1><span style="white-space: pre-wrap">返回描述</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> obj</span></td><td key=1><span style="white-space: pre-wrap"></span></td><td key=2><span>object</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-0><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> id</span></td><td key=1><span style="white-space: pre-wrap">采集点主键ID</span></td><td key=2><span>number</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-1><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> pointNo</span></td><td key=1><span style="white-space: pre-wrap">采集点编码</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-2><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> name</span></td><td key=1><span style="white-space: pre-wrap">采集点名称</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-3><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> version</span></td><td key=1><span style="white-space: pre-wrap">采集点版本</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-4><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> description</span></td><td key=1><span style="white-space: pre-wrap">采集点描述</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-5><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> pointStatus</span></td><td key=1><span style="white-space: pre-wrap">采集点状态</span></td><td key=2><span>number</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-6><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> isAuth</span></td><td key=1><span style="white-space: pre-wrap">是否授权该采集点</span></td><td key=2><span>boolean</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> purposeModels</span></td><td key=1><span style="white-space: pre-wrap"></span></td><td key=2><span>object []</span></td><td key=3>N</td><td key=4><span key=3></span></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7-0><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> id</span></td><td key=1><span style="white-space: pre-wrap">目的ID</span></td><td key=2><span>number</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7-1><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> purposeNo</span></td><td key=1><span style="white-space: pre-wrap">目的编号</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7-2><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> name</span></td><td key=1><span style="white-space: pre-wrap">目的名称</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7-3><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> version</span></td><td key=1><span style="white-space: pre-wrap">目的版本</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7-4><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> description</span></td><td key=1><span style="white-space: pre-wrap">目的描述</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7-5><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> isRequired</span></td><td key=1><span style="white-space: pre-wrap">目的是否必填</span></td><td key=2><span>boolean</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1-7-6><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> collectionStatus</span></td><td key=1><span style="white-space: pre-wrap">采集状态（同意/不同意）</span></td><td key=2><span>number</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> result</span></td><td key=1><span style="white-space: pre-wrap">返回code码</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap">0000代表成功</span></td></tr><tr key=0-3><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> serverTime</span></td><td key=1><span style="white-space: pre-wrap">调用时间</span></td><td key=2><span>number</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr>
               </tbody>
              </table>



#### 附2. 接口名称：用户授权采集


<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th><th key=explain>说明</th>
    </tr>
  </thead>
  <tbody className="ant-table-tbody">
                <tr> <td>请求地址</td> <td>/auth/accountAuth</td> </tr>
                <tr> <td>请求类型</td> <td>POST</td> </tr>
                <span>
  <tr> <td style='width:150px'>概要说明</td> <td></td> </tr>
  </span> 
                <span>
  <tr> <td style='width:150px'>逻辑说明</td> <td>timeMillis：时间戳：毫秒<br/>authKey：subjectData&pointNo&timeMillis&a4637ee5cce28188926906e368567e9c<br/>调用时间±5分钟内有效</td> </tr>
  </span> 
                <tr><td</tr>
              </tbody>
              </table>


##### 请求参数

<h6>Headers</h6>

| 参数名称     | 参数值           | 必须 | 示例 | 备注 |
| ------------ | ---------------- | ---- | ---- | ---- |
| Content-Type | application/json | Y    |      |      |


<h6>Body</h6>

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>参数</th><th key=desc>名称</th><th key=type>类型</th><th key=required>必须</th><th key=sub>长度</th><th key=desc>说明</th>
    </tr>
  </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> pointNo</span></td><td key=1><span style="white-space: pre-wrap">采集点编码</span></td><td key=2><span>string</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> subjectData</span></td><td key=1><span style="white-space: pre-wrap">数据主体信息</span></td><td key=2><span>string</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> purposeModels</span></td><td key=1><span style="white-space: pre-wrap"></span></td><td key=2><span>object []</span></td><td key=3>Y</td><td key=4><span key=3></span></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2-0><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> id</span></td><td key=1><span style="white-space: pre-wrap">目的ID</span></td><td key=2><span>number</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2-1><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> purposeNo</span></td><td key=1><span style="white-space: pre-wrap">目的编号</span></td><td key=2><span>string</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2-2><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> name</span></td><td key=1><span style="white-space: pre-wrap">目的名称</span></td><td key=2><span>string</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2-3><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> version</span></td><td key=1><span style="white-space: pre-wrap">目的版本</span></td><td key=2><span>string</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2-4><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> collectionStatus</span></td><td key=1><span style="white-space: pre-wrap">采集状态（同意/不同意）</span></td><td key=2><span>number</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap">0:不同意  1：同意</span></td></tr><tr key=0-3><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> timeMillis</span></td><td key=1><span style="white-space: pre-wrap">发起授权时间</span></td><td key=2><span>number</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap">时间戳：毫秒</span></td></tr><tr key=0-4><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> authKey</span></td><td key=1><span style="white-space: pre-wrap">授权KEY</span></td><td key=2><span>string</span></td><td key=3>Y</td><td key=4></td><td key=5><span style="white-space: pre-wrap">subjectData&pointNo&timeMillis&a4637ee5cce28188926906e368567e9c</span></td></tr>
               </tbody>
              </table>

##### 响应参数

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>参数</th><th key=desc>名称</th><th key=type>类型</th><th key=required>必须</th><th key=sub>长度</th><th key=desc>说明</th>
    </tr>
  </thead><tbody className="ant-table-tbody">
    <tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> msg</span></td><td key=1><span style="white-space: pre-wrap">返回描述</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr><tr key=0-2><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> result</span></td><td key=1><span style="white-space: pre-wrap">返回code码</span></td><td key=2><span>string</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap">0000代表成功</span></td></tr><tr key=0-3><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> serverTime</span></td><td key=1><span style="white-space: pre-wrap">调用时间</span></td><td key=2><span>number</span></td><td key=3>N</td><td key=4></td><td key=5><span style="white-space: pre-wrap"></span></td></tr>
               </tbody>
              </table>


#### 
