package com.yh.base.constant

/**
 * @description $
 * @date: 2021/5/13 1:35 PM
 * @author: zengbobo
 */
object LoadingCode {

    const val DEFAULT_FLAG: Int = 0x1 shl 31
//
//    const val FLAG_NO_TOAST: Int = 0x1
//    const val FLAG_LOADING: Int = 0x1 shl 1
//    const val FLAG_ERROR_LAYER: Int = 0x1 shl 2

    /***************************** request *****************************/
//    //请求无交互
//    const val REQUESTE_LOADING_UNKNOW: Int = 0
//
//    //request---页面空白方式发起请求（类似：布局里面动画加载）----返回出错页面直接盖ErrorLayer
//    const val REQUEST_LOADING_ANIM: Int = 1
//
//    //request---页面已经有内容发起请求---返回出错有toast提示
//    const val REQUEST_LOADING_DIALOG: Int = 2
//
//    //request---页面已经有内容发起请求---返回出错无toast提示
//    const val REQUEST_LOADING_DIALOG_NO_TOAST: Int = 3
//
//    //request---页面上下拉刷新（无ANIM、无DIALOG）---返回出错有toast提示
//    const val REQUEST_LOADING_PULL: Int = 4
//
//    //request---页面上下拉刷新（无ANIM、无DIALOG）---返回出错无toast提示
//    const val REQUEST_LOADING_PULL_NO_TOAST: Int = 5
//
//    //request---页面上下拉刷新（无ANIM、无DIALOG）---返回出错页面直接盖ErrorLayer
//    const val REQUEST_LOADING_PULL_ERROR_LAYER: Int = 6
//
//    /***************************** response *****************************/
//    //response---无交互
//    const val RESPONSE_LOADING_UNKNOW: Int = 100
//
//    //response---对应 REQUEST_ANIM
//    const val RESPONSE_LOADING_ANIM_SHOW: Int = 101
//    const val RESPONSE_LOADING_ANIM_HIDE: Int = 102
//
//    //response---对应 REQUEST_DIALOG
//    const val RESPONSE_LOADING_DIALOG_SHOW: Int = 103
//    const val RESPONSE_LOADING_DIALOG_HIDE: Int = 104
//
//    //response---弹toast提示
//    const val RESPONSE_LOADING_TOAST: Int = 105
//
//    //response---覆盖出错提示页面(服务器返回code!=Code.SUCCESS)
//    const val RESPONSE_LOADING_ERROR_DATA: Int = 106
//
//    //response---覆盖出错提示页面(http出错)
//    const val RESPONSE_LOADING_ERROR_INTERNET: Int = 107
//
//    //request---页面上下拉刷新 请求完成，成功不做处理
//    const val RESPONSE_LOADING_PULL: Int = 108
}