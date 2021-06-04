package com.yh.base.ui


/**
 * @description 约束应用层代码规范，方便阅读
 *
 * @date: 2021/4/6 4:11 PM
 * @author: zengbobo
 */
interface IDelegateUI {

    /**
     *
     * @description  初始化一些事情
     * @time 2021/4/6 7:10 PM
     * @author zengbobo
     */
    fun ensureInit()

    /**
     * @description 初始化参数
     * @time 2021/4/7 11:03 AM
     * @author zengbobo
     */
    fun initParams()

    /**
     *
     * @description  初始化View
     * @time 2021/4/6 7:00 PM
     * @author zengbobo
     */
    fun initView()

    /**
     * @description 状态栏
     * @time 2021/4/13 7:52 PM
     * @author zengbobo
     */
    fun initImmersionBar()

    /**
     * @description 初始化事件在里面写
     * @time 2021/4/6 7:01 PM
     * @author zengbobo
     */
    fun initListener()

    /**
     *
     * @param isRegister 是否注册,true注册，false移除注册
     * @description  订阅观察在这个方法里面写
     * @time 2021/4/6 7:01 PM
     * @author zengbobo
     */
    fun observer(isRegister:Boolean)

   /**
    *
    * @description   系统生命周期注册（只需要注册，不需要移除注册）
    * @time 2021/4/12 7:30 PM
    * @author zengbobo
    */
    fun lifecycleObserver()

    /**
     * 第一次进来
     *
     * @description 加载数据
     * @time 2021/4/6 7:01 PM
     * @author zengbobo
     */
    fun loadData()


    /**
     * 第一次进来
     *
     * @description 加载数据
     * @time 2021/4/6 7:01 PM
     * @author zengbobo
     */
    fun reloadData()
}