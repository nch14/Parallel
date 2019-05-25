This is an framework for concurrent tasks.

It use CompletableFuture and provides fluent api.

Use Parallel and start from now.

## How to use ?

First, you need implement ConcurrentFactory and ConcurrentConfigFactory for your own. I provide you an example.

Then, you need use Spring manage your beans and scan 'cn.chenhaonee.paralle'.

When you done those, you can use Paralle now.

```
    @Autowired
    private ConcurrentService concurrentService;
    
    SyncResult<Integer, String> result = Parallel.of(bizIds)
            .timeout(1000, TimeUnit.SECONDS) //default for 5000 millseconds. This step is Optional
            .allowPartSuccess(true) //default for false. This step is Optional
            .executor(concurrentService) //must set our ConcurrentService here
            .submit(bizId -> {
                MoreResource moreResource = preparePartDownload(bizId, origin);
                log.info("Start download part {}/{}", bizId + 1, packs);
                down(moreResource, bizId);
                return moreResource.getTarget();
            });
    if you do not need result back, you can use excute other than submit.
    excute method will back you only excution status while submit method will give your function result.
```


这是一个并发任务调用框架

使用线程池与CompletableFuture实现。提供了Fluent Api便于使用。

使用Parallel类，开始体验。

## 如何使用

首先，你需要实现自己的ConcurrentFactory类与ConcurrentConfigFactory类，我提供了一个示例。

接着，你需要使用Spring管理你的这些bean，并且把'cn.chenhaonee.paralle'添加到包扫描的路径下。

完成上面两个步骤以后，就可以使用Parallel的流式API了。示例见上面的代码。

execute与submit方法的区别在于，execute的入参为Consumer，submit的入参为Function