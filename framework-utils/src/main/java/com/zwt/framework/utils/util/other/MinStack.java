package com.zwt.framework.utils.util.other;

import java.util.EmptyStackException;

/**
 * @author zwt
 * @detail  可以获取最小值的栈
 * @date 2018/9/30
 * @since 1.0
 */
public class MinStack {

        //栈元素组
        private  int [] members;
        //指针
        private int size;

        //自定义初始栈长度
        public MinStack(int initCapacity){
            if(initCapacity<=0) {
                throw new RuntimeException("容量不能小于0");
            }
            this.members=new int[initCapacity];
        }

        //默认栈长度为10
        public MinStack() {
            this.members=new int[10];
        }

        //元素入栈
        public synchronized void push(int o){
            ensureCapacity(size+1);
            members[size++]=o;
        }

        //元素出栈
        public synchronized int pop(){
            if(size<=0) {
                throw new EmptyStackException();
            }
            return members[--size];
        }
        //查看栈顶元素
        public synchronized int peek(){
            if(size<=0) {
                throw new EmptyStackException();
            }
            return members[size-1];
        }


        //确认容量
        private synchronized void ensureCapacity(int minCapacity) {
            //size+1比数组长度要长，扩容
            if(minCapacity-members.length>0) {
                int oldCapacity = members.length;
                int[] oldMembers=members;
                //扩容到二倍
                int newCapacity = 2 * oldCapacity ;
                //扩容后还不够或者超过int最大值，就直接赋值size+1
                if (newCapacity - minCapacity < 0)
                    newCapacity = minCapacity;
                members=new int[newCapacity];
                //拷贝数组
                System.arraycopy(oldMembers, 0, members, 0, size);
            }
        }

        //获取栈里的最小值
        public int getMin(){
            if(size<=0) {
                throw new EmptyStackException();
            }
            int min=members[0];
            if(size>1){
                for(int i=1;i<size;i++){
                    if(members[i]<min){
                        min=members[i];
                    }
                }
            }
            return min;
        }

        //获取栈的最大值
        public int getMax(){
            if(size<=0) {
                throw new EmptyStackException();
         }
            int max=members[0];
            if(size>1){
                for(int i=1;i<size;i++){
                  if(members[i]>max){
                     max=members[i];
                  }
              }
          }
         return max;
      }

    public static void main(String[] args) {
        MinStack minStack=new MinStack();
        minStack.push(100);
        minStack.push(2);
        minStack.push(3);
        minStack.push(4);
        minStack.push(5);
        minStack.push(6);
        minStack.push(7);
        minStack.push(8);
        minStack.push(9);
        minStack.push(18);
        minStack.push(11);
        minStack.push(12);
        minStack.push(13);

        System.out.println(minStack.getMin());
        System.out.println(minStack.getMax());
        System.out.println(minStack.pop());
        System.out.println(minStack.getMin());
        System.out.println(minStack.getMax());
        System.out.println(minStack.peek());

    }
}
