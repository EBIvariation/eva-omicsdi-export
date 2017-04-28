package uk.ac.ebi.eva.bd2k;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class StudyExporterCommandLine {

    @Parameter(names = "--outdir", description = "Output directory", required = true,
            validateWith = StudyExporterCommandLine.DirectoryValidator.class)
    private String outputDirectory;

    @Parameter(names = "--help", description = "Command line help˜", help = true)
    private boolean help = false;

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public boolean printHelp() {
        return help;
    }

    public static class DirectoryValidator implements IParameterValidator {

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (!Files.isDirectory(Paths.get(value))) {
                throw new ParameterException(value + " is not a directory");
            }
        }
    }
}
