package halin.rotateimageview;

import java.util.Observable;

/**
 * Created by Administrator on 2015/11/11.
 */

public class ObserverManage extends Observable {


    private   static ObserverManage  myobserverManage =null;


    public  static ObserverManage  getObserverManage()
    {
        if(myobserverManage ==null)
        {
            myobserverManage=new ObserverManage();
        }

        return  	myobserverManage;
    }


    public  void  setMessage(Object  data)
    {
        myobserverManage.setChanged();

        myobserverManage.notifyObservers(data);
    }


}
