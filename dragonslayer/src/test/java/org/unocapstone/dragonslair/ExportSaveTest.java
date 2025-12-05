package org.unocapstone.dragonslair;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import org.junit.jupiter.api.AfterEach;
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
            // at this point the controller will handle the button press, so we wait
            WaitForAsyncUtils.waitForFxEvents();

            // Wait for file to exist
            File expectedFile = createdFiles.get(createdFiles.size() - 1);
            int waitCount = 0;
            while (!expectedFile.exists() && waitCount < 50) {
                Thread.sleep(50);
                waitCount++;
            }

            // Look for "save success!" alert
            try {
                // paritcularly the "OK" button
                robot.clickOn("OK");
                alertCount++;
                System.out.println("Alert Dismissed!");
            } catch (Exception e) {
                System.err.println("WARNING: Could not find success alert for iteration " + currIterations);
            }

            // small delay
            Thread.sleep(50);
        }

        // Verify results
        System.out.println("\n========== TEST RESULTS ==========");
        System.out.println("Button clicks: " + TEST_ITERATIONS);
        System.out.println("Files tracked: " + createdFiles.size());
        System.out.println("Alerts seen: " + alertCount);
        System.out.println("==================================\n");

        assertEquals(TEST_ITERATIONS, createdFiles.size(), "Should have tracked " + TEST_ITERATIONS + " files");
        assertEquals(TEST_ITERATIONS, alertCount, "Should have seen " + TEST_ITERATIONS + " success alerts");

        // Verify files were actually created
        int filesExist = 0;
        for (File file : createdFiles) {
            if (file.exists()) {
                filesExist++;
            }
        }
        assertEquals(TEST_ITERATIONS, filesExist, "All " + TEST_ITERATIONS + " files should exist");
    }

    @AfterEach
    void cleanupTestFiles() {
        System.out.println("Cleaning up test files...");
        int deletedCount = 0;

        // Delete all created files
        for (File file : createdFiles) {
            if (file.exists()) {
                if (file.delete()) {
                    deletedCount++;
                } else {
                    System.err.println("Failed to delete: " + file.getAbsolutePath());
                }
            }
        }

        // Delete the test output directory
        try {
            Path outputDir = Paths.get(TEST_OUTPUT_DIRECTORY);
            if (Files.exists(outputDir)) {
                Files.delete(outputDir);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete output directory: " + e.getMessage());
        }

        System.out.println("Deleted " + deletedCount + " out of " + createdFiles.size() + " files");
        createdFiles.clear();
    }
}
