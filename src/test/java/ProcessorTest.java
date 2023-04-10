
import com.example.trade_processor.DataTypeSorter;
import com.example.trade_processor.GraphPanel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorTest {
    @Test
    public void testProcessData() {
        String[] rows = {"52924702,AAA,13,1136", "52924702,AAC,20,477", "52925641,AAB,31,907", "52927350,AAB,29,724", "52927783,AAC,21,638", "52930489,AAA,18,1222", "52931654,AAA,9,1077", "52933453,AAB,9,756"};
        DataTypeSorter sorter = new DataTypeSorter(new GraphPanel("/"), "");
        for (String r : rows) {
            sorter.updateRecords(r);
        }
        Set<String> res = new HashSet<>(Arrays.asList(sorter.getRecords()));

        Set<String> trueAnswer = new HashSet<>(Arrays.asList("AAA,5787,40,1161,1077,1222", "AAB,6103,69,810,724,907", "AAC,3081,41,559,477,638"));

        assertEquals(res.size(), trueAnswer.size());
        assertEquals(res, trueAnswer);

    }
    // Test the MaxTimeGap for symbol with only one trade
    @Test
    public void testProcessData2() {
        String[] rows = {"52924702,AAA,13,1136", "52924702,AAC,20,477", "52925641,AAB,20,18", "52927350,AAB,5,7"};
        DataTypeSorter sorter = new DataTypeSorter(new GraphPanel("/"), "");
        for (String r : rows) {
            sorter.updateRecords(r);
        }
        Set<String> res = new HashSet<>(Arrays.asList(sorter.getRecords()));

        Set<String> trueAnswer = new HashSet<>(Arrays.asList("AAA,0,13,1136,1136,1136", "AAC,0,20,477,477,477", "AAB,1709,25,15,7,18"));

        assertEquals(res.size(), trueAnswer.size());
        assertEquals(res, trueAnswer);

    }

}
