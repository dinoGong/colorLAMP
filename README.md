# colorLAMP
彩色蓝牙变色遥控灯

该项目由几个部分组成

1）led灯
2）arduino nano
3）CC41-A 蓝牙从机模块
4）android APP


led灯由3个led发光二极管组成，分别是红、绿、蓝。
arduino nano 主要用到tx、rx串口和数字针脚。接收处理 蓝牙 发过来的 串口数据，然后对针脚进行PWM操作。

CC41-A 是蓝牙4.0从机模块。因为这个彩色灯并没有驱动220v的灯泡，所以没有用到继电器。

android APP 就是用来连接 CC41-A 发送指令的。

具体看代码就好了 ^_^

