package org.unocapstone.dragonslair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.testfx.api.FxRobot;
import org.junit.jupiter.api.Test;

public class ExportSaveTest extends BaseFxUiTest {
    // Should be changed if a higher amount is warranted
    private static final int    TEST_ITERATIONS = 100;
    private static final String TEST_OUTPUT_DIRECTORY = "test_export_spam";
    private int currIterations = 0;

    // Contains a list of created files, will be constantly deleted as test runs
    private List<File> createdFiles = new ArrayList<File>();
    private int alertCount = 0;

    @Test
    void testExport(FxRobot robot) throws Exception {
        // Create the output directory for the test exports
        Path outputDirectory = Paths.get(TEST_OUTPUT_DIRECTORY);
        if (!Files.exists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
        }

    // Inject the custom FileChooser behavior into the controller
        controller.setFileChooserProvider((fc, window) -> { // recall that controller is defined within the abstract class
            String fileName = TEST_OUTPUT_DIRECTORY + "/flagged_export_" + currIterations + ".xlsx";
            File file = new File(fileName);
            createdFiles.add(file);
            return file;
        });

        openReportsTab(robot);
        
        // Testing body
        for(int i = 0; i < TEST_ITERATIONS; i++) {
            currIterations += 1;
            System.out.println("============== Test " + currIterations + " ====================");
            
            // click the export button
            robot.clickOn("#exportNewWeekFlagsCustomerRequestsButton");
            
        }
    }
}
