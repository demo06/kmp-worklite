package funny.buildapp.common.ui.route

import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator


/**
 * 路由名称
 */
public object RouteUtils {

    public const val STEAD_SYMBOL: String = "^0^"


    public fun navTo(navCtrl: Navigator,route:String,args:Any?,options: NavOptions?=null){
        navCtrl.navigate("$route/$args",options)
    }

    public fun Navigator.back(){
        this.goBack()
    }

    /**
     * 各个序列化的参数类的key名
     */
    private const val ARGS = "args"


}