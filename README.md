# blogspring
搭建blog全家桶旗下的springboot示例项目  

-------

### 测试
项目运行起来后，  
使用登录 https://ids.neters.club 的账号`Token` ，测试接口`http://localhost:8181/api/blog`即可，  
因为本项目只校验了`Scope`：  
```
.withArrayClaim("scope","blog.core.api")
```

完整的请求： 
```
curl --location --request GET 'http://localhost:8181/api/blog' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsI......'
```


-------
  
### 目的：   

- [ ] 给 `.NET Core` 学习者一个了解`java`的机会，其实两者真的很像；
- [ ] 测试集成 `Blog.Idp` 的`ids4`统一认证平台；
- [ ] 做对比，测性能；
