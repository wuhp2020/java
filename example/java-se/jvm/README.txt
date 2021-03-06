java内存模型:
Java堆（Heap）:
是Java虚拟机所管理的内存中最大的一块. Java堆是被所有线程共享的一块内存区域, 在虚拟机启动时创建.
此内存区域的唯一目的就是存放对象实例, 几乎所有的对象实例都在这里分配内存.

方法区（Method Area）:
与Java堆一样, 是各个线程共享的内存区域,
它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据.

程序计数器（Program Counter Register）:
是一块较小的内存空间, 它的作用可以看做是当前线程所执行的字节码的行号指示器.

JVM栈（JVM Stacks）:
与程序计数器一样, Java虚拟机栈（Java Virtual Machine Stacks）也是线程私有的, 它的生命周期与线程相同.
虚拟机栈描述的是Java方法执行的内存模型: 每个方法被执行的时候都会同时创建一个栈帧（Stack Frame）,
用于存储局部变量表、操作栈、动态链接、方法出口等信息. 每一个方法被调用直至执行完成的过程,
就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程.

本地方法栈（Native Method Stacks）:
与虚拟机栈所发挥的作用是非常相似的, 其区别不过是虚拟机栈为虚拟机执行Java方法（也就是字节码）服务,
而本地方法栈则是为虚拟机使用到的Native方法服务.

#####################################

对象分配规则:
对象优先分配在Eden区, 如果Eden区没有足够的空间时, 虚拟机执行一次Minor GC.

大对象直接进入老年代（大对象是指需要大量连续内存空间的对象）,
这样做的目的是避免在Eden区和两个Survivor区之间发生大量的内存拷贝（新生代采用复制算法收集内存）.

长期存活的对象进入老年代, 虚拟机为每个对象定义了一个年龄计数器,
如果对象经过了1次Minor GC那么对象会进入Survivor区, 之后每经过一次Minor GC那么对象的年龄加1, 知道达到阀值对象进入老年区.

动态判断对象的年龄, 如果Survivor区中相同年龄的所有对象大小的总和大于Survivor空间的一半,
年龄大于或等于该年龄的对象可以直接进入老年代.

空间分配担保, 每次进行Minor GC时, JVM会计算Survivor区移至老年区的对象的平均大小,
如果这个值大于老年区的剩余值大小则进行一次Full GC, 如果小于,
检查HandlePromotionFailure设置, 如果true则只进行Monitor GC, 如果false则进行Full GC.

#####################################

JVM加载class文件的原理机制:
类的加载是指把类的.class文件中的数据读入到内存中, 通常是创建一个字节数组读入.class文件,
然后产生与所加载类对应的Class对象. 加载完成后, Class对象还不完整, 所以此时的类还不可用.
当类被加载后就进入连接阶段, 这一阶段包括验证、准备（为静态变量分配内存并设置默认的初始值）和解析（将符号引用替换为直接引用）三个步骤.
最后JVM对类进行初始化, 包括:
1)如果类存在直接的父类并且这个类还没有被初始化, 那么就先初始化父类.
2)如果类中存在初始化语句, 就依次执行这些初始化语句.

#####################################

类的生命周期:
加载: 查找并加载类的二进制数据, 在Java堆中也创建一个java.lang.Class类的对象
连接: 连接又包含三块内容: 验证、准备、初始化
1）验证: 文件格式、元数据、字节码、符号引用验证
2）准备: 为类的静态变量分配内存, 并将其初始化为默认值
3）解析: 把类中的符号引用转换为直接引用
初始化: 为类的静态变量赋予正确的初始值
使用: new出对象程序中使用
卸载: 执行垃圾回收

#####################################

7种垃圾回收器:

1、Serial收集器
新生代单线程收集器, 简单高效.

2、ParNew收集器
Serial收集器的多线程版本, 除了在垃圾回收时使用多线程, 其余都和Serial收集器相同.

3、Parallel Scavenge收集器
Parallel Scavenge收集器是并行的采用复制算法的新生代收集器, 着重于系统的吞吐量, 适合后台运算而不需要太多用户交互的任务.

4、Serial Old收集器
Serial Old是单线程的老年代垃圾收集器, 使用标记-整理算法, 具有简单高效的特点.

5、Parallel Old收集器
Parallel Old收集器是Parallel Scanenge的老年代版本, 多线程垃圾回收, 也是用标记-整理算法.

6、CMS收集器
CMS注重服务的响应时间, 是基于标记-清除算法实现, 具有并发收集、低停顿的特点.

7、G1收集器
Garbage First, 基于标记-整理算法, 其将整个Java堆（包括新生代和老年代）划分为多个大小固定的独立区域,
并且跟踪这些区域里的垃圾堆积程度, 在后台维护一个优先列表, 每次根据允许的收集时间, 优先回收垃圾最多的区域.

#####################################

并发收集通常包括以下步骤:

初始化标记:  Stop The World
暂停应用程序线程, 遍历GC ROOTS直接可达的对象并将其压入标记栈(mark-stack), 标记完之后恢复应用程序线程

并发标记:
这个阶段虚拟机会分出若干线程(GC 线程)去进行并发标记, 标记哪些对象呢, 标记那些GC ROOTS最终可达的对象
具体做法是推出标记栈里面的对象, 然后递归标记其直接引用的子对象, 同样的把子对象压到标记栈中, 重复推出, 压入...
直至清空标记栈, 这个阶段GC线程和应用程序线程同时运行

重新标记:   Stop The World
重新标记可以理解成一个同步刷新对象间引用的操作, 整个过程是STW. 在并发标记其间, 应用程序不断变更对象引用,
此时的GC ROOTS有可能会发生变化, 这个时候需要同步更新这个增量变化,
于是重新从当前的GC ROOTS和指针更新的区域出发(mod-union table)再进行一次标记,
所以这个过程被叫作重新标记, 需要注意的是: 已经标记的对象是不会再遍历一次, 标记线程识别对象在并发阶段已经标记过了, 就会跳过该对象
所以重新标记只会遍历那些新增没有标记过的活动对象和其间有指针更新的活动对象, 如果指针更新频繁,
重新标记很有可能会遍历新生代中的大部分甚至全部对象. 所以如果重新标记阶段很慢, 可以启动一次YGC,
来减少并发标记的工作量减少其停顿时间

并发清除:
重新标记结束后, 应用程序继续运行, 此时分出一个处理器去进行垃圾回收工作
老年代的对象通常是存活时间长, 回收比例低, 所以采用的回收算法是标记-清除. 这个阶段GC回收线程是遍历整个老年代,
遇到没有被标记的对象(垃圾)就清空掉相应的内存块, 并加入可分配列表. 遇到被标记的对象保持原来的位置不动,
只是重置其标记位, 用于下一次GC

并发重置:
oracle官方文档中描述这个阶段的工作是: 重新调整堆的大小, 并为下一次GC做好数据结构支持, 比如重置卡表的标位

#####################################

如何判断对象已经死亡?
1、引用计数法
在对象中添加一个引用计数器, 当有个地方引用时, 计数器值+1, 当引用失效时, 计数器值-1.
计数器为0的对象就是死亡的, 但是这里有个问题: 对象循环引用, 两个对象互相引用着对方, 导致它们的引用计数器不为0, 于是无法通知GC回收.

2、GC Roots搜索
Java中采用的是GC ROOT搜索, 思路就是通过一系列名为“GC Roots”的对象作为起点, 从这些节点开始向下搜索,
搜索中所走过的路径称为引用链, 当GC Roots对某一对象不可达时, 则证明此对象不可用.
GC Roots包括以下几种:
栈帧中的本地变量表中引用的对象
方法区中类静态属性引用的对象
方法区中常量引用的对象
本地方法栈Native方法引用的对象

#####################################

什么时候会触发Full GC:

1. 调用 System.gc()
只是建议虚拟机执行 Full GC, 但是虚拟机不一定真正去执行. 不建议使用这种方式, 而是让虚拟机管理内存.

2. 未指定老年代和新生代大小, 堆伸缩时会产生fullgc, 所以一定要配置-Xmx、-Xms

3. 老年代空间不足
老年代空间不足的常见场景比如大对象、大数组直接进入老年代、长期存活的对象进入老年代等.
为了避免以上原因引起的 Full GC, 应当尽量不要创建过大的对象以及数组.
除此之外, 可以通过 -Xmn 虚拟机参数调大新生代的大小, 让对象尽量在新生代被回收掉, 不进入老年代.
还可以通过 -XX:MaxTenuringThreshold 调大对象进入老年代的年龄, 让对象在新生代多存活一段时间.
在执行Full GC后空间仍然不足, 则抛出错误：java.lang.OutOfMemoryError: Java heap space


#####################################

元空间:
JDK1.8对JVM架构的改造将类元数据放到本地内存中, 另外, 将常量池和静态变量放到Java堆里.
HotSpot VM将会为类的元数据明确分配和释放本地内存.
在这种架构下, 类元信息就突破了原来-XX:MaxPermSize的限制, 现在可以使用更多的本地内存.
这样就从一定程度上解决了原来在运行时生成大量类造成经常Full GC问题, 如运行时使用反射、代理等.
所以升级以后Java堆空间可能会增加。

元空间的本质和永久代类似, 都是对JVM规范中方法区的实现. 不过元空间与永久代之间的最大区别在于:
元空间并不在虚拟机中, 而是使用本地内存. 因此, 默认情况下, 元空间的大小仅受本地内存限制, 但可以通过以下参数指定元空间的大小:
-XX:MetaspaceSize, 初始空间大小, 达到该值就会触发垃圾收集进行类型卸载, 同时GC会对改值进行调整:
如果释放了大量的空间, 就适当降低该值, 如果释放了很少的空间, 那么在不超过MaxMetaspaceSize时, 适当提高该值.

-XX:MaxMetaspaceSize, 最大空间, 默认是没有限制的.
除了上面的两个指定大小的选项外, 还有两个与GC相关的属性:
-XX:MinMetaspaceFreeRatio, 在GC之后, 最小的Metaspace剩余空间容量的百分比, 减少为分配空间所导致的垃圾收集.
-XX:MaxMetaspaceFreeRatio, 在GC之后, 最大的Metaspace剩余空间容量的百分比, 减少为释放空间所导致的垃圾收集.

#####################################

内存溢出及配置

1.堆
设置jvm值的方法是通过-Xms(堆的最小值), -Xmx(堆的最大值)
java.lang.OutOfMemoryError:Javaheapspace
    这种是java堆内存不够, 一个原因是真不够（如递归的层数太多等）, 另一个原因是程序中有死循环
　　如果是java堆内存不够的话, 可以通过调整JVM下面的配置来解决:
　　-Xms3062m
　　-Xmx3062m

java.lang.OutOfMemoryError:GCoverheadlimitexceeded
　　JDK6新增错误类型, 当GC为释放很小空间占用大量时间时抛出, 一般是因为堆太小, 导致异常的原因, 没有足够的内存
　　1、查看系统是否有使用大内存的代码或死循环
　　2、通过添加JVM配置, 来限制使用内存:
　　-XX:-UseGCOverheadLimit

2.栈
设置栈大小的方法是设置-Xss参数
java.lang.StackOverflowError
　　这也内存溢出错误的一种, 即线程栈的溢出, 要么是方法调用层次过多（比如存在无限递归调用）, 要么是线程栈太小
　　优化程序设计, 减少方法调用层次, 调整-Xss参数增加线程栈大小

java.lang.OutOfMemoryError:unabletocreatenewnativethread
　　Stack空间不足以创建额外的线程, 要么是创建的线程过多, 要么是Stack空间确实小了
　　由于JVM没有提供参数设置总的stack空间大小, 但可以设置单个线程栈的大小, 而系统的用户空间一共是3G,
　　除了Text/Data/BSS/MemoryMapping几个段之外, Heap和Stack空间的总量有限, 是此消彼长的, 因此遇到这个错误,
　　可以通过两个途径解决:
    1.通过-Xss启动参数减少单个线程栈大小, 这样便能开更多线程（当然不能太小，太小会出现StackOverflowError）
　　 2.通过-Xms-Xmx两参数减少Heap大小, 将内存让给Stack（前提是保证Heap空间够用）

3.PermGen space
第三个异常是关于perm的异常内容, 我们需要的是设置方法区的大小, 实现方式是通过设置-XX:PermSize和-XX:MaxPermSize参数
java.lang.OutOfMemoryError:PermGenspace 这种是P区内存不够, 可通过调整JVM的配置:
　　-XX:MaxPermSize=128m
　　-XXermSize=128m
　　JVM的Perm区主要用于存放Class和Meta信息的, Class在被Loader时就会被放到PermGenspace, 这个区域成为年老代,
    GC在主程序运行期间不会对年老区进行清理, 默认是64M大小, 当程序需要加载的对象比较多时,
    超过64M就会报这部分内存溢出了, 需要加大内存分配, 一般128m足够

4.DirectMemory
第四个异常估计遇到的人就不多了, 是DirectMemory内存相关的
java.lang.OutOfMemoryError:Directbuffermemory
　　调整-XX: MaxDirectMemorySize=参数, 如添加JVM配置
　　-XX:MaxDirectMemorySize=128m

内存使用监控工具jvmstat

#####################################

jvm调优命令:

CPU占用过高问题排查
1、linux查看进程信息
top

2、查看进程占用cpu最多的线程
ps -mp pid -o THREAD,tid,time

3、线程ID转16进制
printf "%x\n" 23968

4、查看线程信息
jstack 23967 | grep -A 10 5da0
jstack 23967 | grep 5da0 -A 30

5、查看进程的对象信息
jmap -histo:live 23967 | more

6、查看进程的GC情况
jstat -gcutil 23967 1000 100

交换1    交换2   伊甸园  老年代              新生代gc 耗时     full-gc  耗时    总耗时
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT
 46.75   0.00  35.61   0.82  94.67  92.89      6    0.239     4    0.156    0.396

7、查看哪个线程占用cpu多
使用 pidstat -p 1 3 -u -t
-p: 指定进程号
-u: 默认的参数，显示各个进程的cpu使用统计
-t: 显示选择任务的线程的统计信息外的额外信息

8、Heap Dump文件分析工具
jhat 是JDK自带的用于分析JVM Heap Dump文件的工具, 使用下面的命令可以将堆文件的分析结果以HTML网页的形式进行展示.
jhat <heap-dump-file>
其中 heap-dump-file 是文件的路径和文件名, 可以使用 -J-Xmx512m 参数设置命令的内存大小. 执行结果:
Snapshot resolved.
Started HTTP server on port 7000
Server is ready.
访问 http://localhost:7000 就可以看到结果

#####################################

1. 为什么栈不用垃圾回收器回收:
虚拟机栈里的栈帧即对应代码中的一个方法. 代码运行的过程, 即栈帧入栈出栈的过程.
一个方法执行完, 栈帧出栈后, 即被销毁, 只有入栈出栈这样简单的操作, 不需要设计复杂的垃圾回收算法来回收.
随着方法的执行, 线程的结束正常回收即可.

在递归函数中, 该方法还没有结束, 就一直不会出栈, 如果循环的次数过多, 栈空间有被挤爆的可能.
会出现StackOverflowError 栈溢出, 栈溢出也是内存溢出的一种情况. 可通过-Xss （stack size）设置栈大小.


2. 为什么程序计数器没有OOM:
程序计数器（Program Counter Register）也称PC寄存器, 是运行时数据区里唯一一块没有Out of Memory的区域.
只存下一个字节码指令的地址, 消耗内存小且固定, 无论方法多深, 他只存一条.
只针对一个线程, 随着线程的结束而销毁.

#####################################

回收方法区
方法区回收价值很低, 主要回收废弃的常量和无用的类.
如何判断无用的类:
1.该类所有实例都被回收（Java堆中没有该类的对象）
2.加载该类的ClassLoader已经被回收
3.该类对应的java.lang.Class对象没有在任何地方被引用, 无法在任何地方利用反射访问该类

#####################################

Minor GC与Full GC分别在什么时候发生:
新生代内存不够用时候发生MGC也叫YGC
JVM内存不够的时候发生FGC

#####################################

G1收集器
G1开创的基于Region的堆内存布局. 虽然G1也仍是遵循分代收集理论设计的, 但其堆内存的布局与其他收集器有非常明显的差异:
G1不再坚持固定大小以及固定数量的分代区域划分, 而是把连续的Java堆划分为多个大小相等的独立区域（Region）,
每一个Region都可以根据需要, 扮演新生代的Eden空间、Survivor空间, 或者老年代空间.

G1中五种不同类型的Region:
Eden regions(年轻代-Eden区)
Survivor regions(年轻代-Survivor区)
Old regions（老年代）
Humongous regions（巨型对象区域, 通常也被认为是老年代的一部分）
Free resgions（未分配区域, 也会叫做可用分区）

#####################################

打破双亲委派机制:（也就是能向下委派和不委派）
打破双亲委派的两种方式:
1.通过spi机制, 使用ServiceLoader.load去加载
2.通过自定义类加载器, 继承classloader, 重写loadclass方法

#####################################


JVM性能调优参数:

java
-server
-Xms3G
-Xmx3G
-Xmn2g
-Xss256k
-XX:PermSize=128m
-XX:MaxPermSize=512m
# 服务器往往可能缺少显示、键盘、鼠标设备, 但又需要使用他们提供的功能, 生成相应的数据, 以提供给客户端
-Djava.awt.headless=true
-Dfile.encoding=utf-8
-XX:+UseConcMarkSweepGC
# CMS垃圾收集器, 当老年代达到75%时, 触发CMS垃圾回收, 必须跟-XX:+UseConcMarkSweepGC、-XX:+UseCMSInitiatingOccupancyOnly两个参数一起使用才能生效
-XX:CMSInitiatingOccupancyFraction=75
-XX:+UseCMSInitiatingOccupancyOnly
# IntegerCache有一个静态代码块, JVM在加载Integer这个类时, 会优先加载静态的代码, 当JVM进程启动完毕后, -128 ~ +127 范围的数字会被缓存起来, 调用valueOf方法的时候, 如果是这个范围内的数字, 则直接从缓存取出, 超过这个范围的, 就只能构造新的Integer对象了
-XX:AutoBoxCacheMax=20000
# 当大量抛出同样的异常的后, 后面的异常输出将不打印堆栈, 打印堆栈的时候底层会调用到Throwable.getOurStackTrace()方法, 而这个方法是synchronized的, 对性能有比较明显对影响
-XX:-OmitStackTraceInFastThrow
-XX:ErrorFile=/app/logs/hs_err_%p.log
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/app/logs/
-Xloggc:/app/logs/gc.log
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-jar java8-1.0.jar

#####################################

指令重排
计算机在执行程序时, 为了提高性能, 编译器和处理器常常会对指令重排, 一般分为以下三种:
源代码 -> 编译器优化的重排 -> 指令并行的重排 -> 内存系统的重排 -> 最终执行指令
单线程环境里面确保最终执行结果和代码顺序的结果一致
处理器在进行重排序时, 必须要考虑指令之间的数据依赖性
多线程环境中线程交替执行, 由于编译器优化重排的存在, 两个线程中使用的变量能否保证一致性是无法确定的, 结果无法预测.
指令重排案例:
public void mySort() {
    int x = 11;
    int y = 12;
    x = x + 5;
    y = x * x;
}
按照正常单线程环境, 执行顺序是 1 2 3 4
但是在多线程环境下, 可能出现以下的顺序:
2 1 3 4
1 3 2 4
上述的过程就可以当做是指令的重排, 即内部执行顺序, 和我们的代码顺序不一样

#####################################

Java内存模型(Java Memory Model, JMM)
1. 一个本地变量可能是原始类型, 在这种情况下, 它总是“呆在”线程栈上.
2. 一个本地变量也可能是指向一个对象的一个引用, 在这种情况下, 引用（这个本地变量）存放在线程栈上, 但是对象本身存放在堆上.
3. 一个对象可能包含方法, 这些方法可能包含本地变量, 这些本地变量仍然存放在线程栈上, 即使这些方法所属的对象存放在堆上.
4. 一个对象的成员变量可能随着这个对象自身存放在堆上, 不管这个成员变量是原始类型还是引用类型.
5. 静态成员变量跟随着类定义一起也存放在堆上.
6. 存放在堆上的对象可以被所有持有对这个对象引用的线程访问, 当一个线程可以访问一个对象时, 它也可以访问这个对象的成员变量,
如果两个线程同时调用同一个对象上的同一个方法, 它们将会都访问这个对象的成员变量, 但是每一个线程都拥有这个成员变量的私有拷贝.

JMM定义了线程和主内存之间的抽象关系:
1. 线程之间的共享变量存储在主内存（Main Memory）中
2. 每个线程都有一个私有的本地内存（Local Memory）, 本地内存是JMM的一个抽象概念, 并不真实存在,
它涵盖了缓存、写缓冲区、寄存器以及其他的硬件和编译器优化, 本地内存中存储了该线程以读/写共享变量的拷贝副本.
4. 从更低的层次来说, 主内存就是硬件的内存, 而为了获取更好的运行速度, 虚拟机及硬件系统可能会让工作内存优先存储于寄存器和高速缓存中.
5. Java内存模型中的线程的工作内存（working memory）是cpu的寄存器和高速缓存的抽象描述,
而JVM的静态内存储模型（JVM内存模型）只是一种对内存的物理划分而已, 它只局限在内存, 而且只局限在JVM的内存.


Java内存模型规则:
1. 如果要把一个变量从主内存中复制到工作内存, 就需要按顺寻地执行read和load操作, 如果把变量从工作内存中同步回主内存中,
就要按顺序地执行store和write操作, 但Java内存模型只要求上述操作必须按顺序执行, 而没有保证必须是连续执行.
2. 不允许read和load、store和write操作之一单独出现
3. 不允许一个线程丢弃它的最近assign的操作, 即变量在工作内存中改变了之后必须同步到主内存中.
4. 不允许一个线程无原因地（没有发生过任何assign操作）把数据从工作内存同步回主内存中.
5. 一个新的变量只能在主内存中诞生, 不允许在工作内存中直接使用一个未被初始化（load或assign）的变量.
即就是对一个变量实施use和store操作之前, 必须先执行过了assign和load操作.
6. 一个变量在同一时刻只允许一条线程对其进行lock操作, 但lock操作可以被同一条线程重复执行多次,
多次执行lock后, 只有执行相同次数的unlock操作, 变量才会被解锁, lock和unlock必须成对出现
7. 如果对一个变量执行lock操作, 将会清空工作内存中此变量的值, 在执行引擎使用这个变量前需要重新执行load或assign操作初始化变量的值
8. 如果一个变量事先没有被lock操作锁定, 则不允许对它执行unlock操作, 也不允许去unlock一个被其他线程锁定的变量.
9. 对一个变量执行unlock操作之前, 必须先把此变量同步到主内存中（执行store和write操作）.
