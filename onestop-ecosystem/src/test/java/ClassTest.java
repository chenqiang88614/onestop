import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.ecosystem.entity.ProdOrder;
import lombok.Data;

import java.lang.reflect.Field;
@Data
class User {
    private String name;
    private Integer age;

}
public class ClassTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(SnowflakeIdWorker.getUUID());
        }
    }
}
