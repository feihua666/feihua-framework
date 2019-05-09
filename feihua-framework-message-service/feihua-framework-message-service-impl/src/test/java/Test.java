import com.feihua.framework.message.handler.BaseVUsersByMessageTargetsIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2019/5/8 18:42
 */
public class Test {
    public static void main(String[] args) {
        List<String> r = new ArrayList<>();

        for (int i = 0; i < 43; i++) {
            r.add(i + "");
        }
        BaseVUsersByMessageTargetsIterator i = new BaseVUsersByMessageTargetsIterator(1,15,"",r);
        List<String> temp = null;
        while ((temp = i.next()) != null){
            System.out.println("********************");
            for (String s : temp) {
                System.out.println(s);
            }
        }
    }
}
