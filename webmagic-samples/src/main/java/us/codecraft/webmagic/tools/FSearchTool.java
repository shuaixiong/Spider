package us.codecraft.webmagic.tools;

import java.lang.reflect.Method;
import java.util.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FSearchTool {
    private StringBuffer mKeyWordString = new StringBuffer();
    private List<Object> mSearchObjs = new ArrayList<Object>();
    private int[] mIndexes;

    public FSearchTool(List<? extends Object> objects, String... fields) throws Exception {
        super();
        init(objects, fields);
    }

    private void init(List<? extends Object> objs, String... fields) throws Exception {
        if (objs != null) {
            mKeyWordString.setLength(0);
            mSearchObjs.clear();
            mSearchObjs = new ArrayList<Object>(objs);
            mIndexes = new int[mSearchObjs.size() * 2];
            int index = 0;
            for (int i = 0; i < mSearchObjs.size(); i++) {
                Object info = mSearchObjs.get(i);
                // 指定要搜索的字段
                String searchKey = getSearchKey(info, fields);
                // 将该字符串在总字符串中的起终位置保存下来,位置是索引值而非长度
                int length = mKeyWordString.length();
                mIndexes[index] = length;
                mKeyWordString.append(searchKey);
                length = mKeyWordString.length();
                index++;
                // 保存新加搜索字段的索引值
                mIndexes[index] = (length > 0) ? length - 1 : 0;
                index++;
            }
        }
    }

    /**
     * 通过反射从对象中取出指定字段的值
     */
    private String getSearchKey(Object obj, String... fields) throws Exception {
        StringBuilder searchKeys = new StringBuilder();
        Class<? extends Object> clazz = obj.getClass();
        try {
            for (String str : fields) {
                // 搜索字段使用空格隔开
                Field f = clazz.getDeclaredField(str);
                f.setAccessible(true);
                Object val = f.get(obj);
                searchKeys.append(val).append(" ");
                f.setAccessible(false);
            }
        } catch (Exception e) {
            throw new Exception("取值异常：" + e.getMessage());
        }
        return searchKeys.toString();
    }

    /**
     * 搜索结果
     *
     * @param keyWords
     *            搜索的关键字，要去掉首尾的空格
     * @return 返回搜索到的对象
     */
    public List<Object> searchTasks(String keyWords) {
        List<Object> searchedTask = new ArrayList<Object>();
        int[] searchIndex = getSearchIndex(keyWords);
        for (int index : searchIndex) {
            if (index != -1 && index < mSearchObjs.size() * 2) {
                Object info = mSearchObjs.get(index / 2);
                if (info != null && !searchedTask.contains(info)) {
                    searchedTask.add(info);
                }
            }
        }
        return searchedTask;
    }

    /**
     * 找到匹配的索引数据
     *
     * @param keyWords
     *            搜索的关键字
     * @return 在初始化的索引数组的下标数组
     */
    private int[] getSearchIndex(String keyWords) {
        Pattern pattern = Pattern.compile(keyWords, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
        Matcher matcher = pattern.matcher(mKeyWordString.toString());
        ArrayList<Integer> searchResult = new ArrayList<Integer>();
        while (matcher.find()) {
            // 不宜在此处再做循环，否则可能造成循环次数过多错误
            searchResult.add(matcher.start());
        }
        int[] searchIndexes = new int[searchResult.size()];
        for (int i = 0; i < searchIndexes.length; i++) {
            int findIndex = findIndex(searchResult.get(i));
            searchIndexes[i] = (findIndex / 2) * 2;
        }
        return searchIndexes;
    }

    /**
     * 使用二分法找到指定字符位置在索引数组中的位置
     *
     * @param charAt
     *            字符在整个字符串中的位置
     * @return 在索引数组中的位置
     */
    private int findIndex(int charAt) {
        int low = 0;
        int high = mIndexes.length - 1;
        int mid = -1;
        while (low <= high) {
            mid = (low + high) >>> 1;
            int midVal = mIndexes[mid];
            if (midVal < charAt) {
                low = mid + 1;
            } else if (midVal > charAt) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return mid;
    }

    /**
     * 根据属性名获取属性值
     * */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取属性名数组
     * */
    private String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    private List getFiledsInfo(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        List list = new ArrayList();
        Map infoMap=null;
        for(int i=0;i<fields.length;i++){
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     * */
    public Object[] getFiledValues(Object o){
        String[] fieldNames=this.getFiledName(o);
        Object[] value=new Object[fieldNames.length];
        for(int i=0;i<fieldNames.length;i++){
            value[i]=this.getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }

    public static void main(String[] args) {

    }


}
