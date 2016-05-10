package com.lefu;

import java.util.Stack;

import android.app.Activity;
/**
 * 去掉了持有Context 对象作为属性，因为这样会导致内存泄露
 * @author Administrator
 * 2014-2-8
 */
public class AM
{
  private static Stack<Activity> stack;
  private static AM am;
  
  private AM(){}
  
  public static AM getIns()
  {
    if (am == null)
    	am = new AM();
    return am;
  }

  public void exit()
  {
	  finishAllActivity();
      System.exit(0);
  }

  public void addActivity(Activity activity)
  {
    if (stack == null)
    	stack = new Stack<Activity>();
    stack.add(activity);
  }

  public Activity currentActivity()
  {
    return (Activity)stack.lastElement();
  }

  public void finishCurrentActivity()
  {
    finishActivity((Activity)stack.lastElement());
  }

  public void finishActivity(Activity activity)
  {
    if (activity != null)
    {
    	stack.remove(activity);
      activity.finish();
    }
  }

  public void finishActivity(Class<Activity> clazz)
  {
    int size = stack.size() ;
    for(int i = 0; i < size ; i++ ){
    	Activity acty =  stack.get(i) ;
    	if (acty.getClass().equals(clazz))
            finishActivity(acty);
    }
  }

  public void finishAllActivity()
  {
	  int size = stack.size() ;
    for(int i = 0; i < size ; i++ ){
    	Activity acty =  stack.pop() ;
        finishActivity(acty);
    }
  }
}