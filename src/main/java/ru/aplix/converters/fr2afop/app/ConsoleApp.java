package ru.aplix.converters.fr2afop.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.MimeConstants;

import ru.aplix.converters.fr2afop.reader.FreeReportReader;
import ru.aplix.converters.fr2afop.reader.FreeReportReader2217;
import ru.aplix.converters.fr2afop.reader.XMLReportReader;
import ru.aplix.converters.fr2afop.utils.CommandLine;

/**
 * Console application for the project.
 */
public class ConsoleApp {

	private Log log = LogFactory.getLog(getClass());

	private List<Command> commands;

	private String fopConfigFile = null;
	private String convConfigFile = null;

	public static void main(String[] args) {
		ConsoleApp ca = new ConsoleApp();
		ca.run(args);
	}

	public ConsoleApp() {
		commands = new ArrayList<Command>();
	}

	private void run(String[] args) {
		try {
			printNameAndVersion();

			if (!validateArgs(new CommandLine(args))) {
				printUsage();
				return;
			}

			for (Command command : commands) {
				if (command instanceof RenderXMLToOutput) {
					((RenderXMLToOutput) command).setFopConfigFileName(fopConfigFile);
				}
				if (command instanceof ConvertReportToXML) {
					((ConvertReportToXML) command).setConvConfigFileName(convConfigFile);
				}

				command.execute();
			}
		} catch (Exception e) {
			log.error(null, e);
		}
	}

	private void printNameAndVersion() throws IOException {
		InputStream is = getClass().getResourceAsStream("/META-INF/version");
		log.info(IOUtils.toString(is));
	}

	private void printUsage() {
		log.info("Usage:");
		log.info("  java -jar fr2afop.jar -i[fr|fr2217|xml|ttf] inputFile -o[xml|pdf|pcl|ps|png|fo] outputFile [-fc fopConfigFile] [-cc convConfigFile]");
		log.info("");
		log.info("Input formats:");
		log.info(" ifr     - reads FreeReport file and converts it to xml or renders it");
		log.info(" ifr2217 - reads FreeReport file in old format (version 2217)");
		log.info(" ixml    - reads XML-file acquired after conversion");
		log.info(" ittf    - reads the TTF file and generates an appropriate font metrics xml-file for it");
		log.info("");
		log.info("Output formats:");
		log.info(" oxml    - specifies XML file conversion");
		log.info(" opdf    - specifies PDF rendering");
		log.info(" opcl    - specifies PCL rendering");
		log.info(" ops     - specifies PostScript rendering");
		log.info(" opng    - specifies PNG rendering");
		log.info(" ofo     - specifies XSL-FO transformation");
		log.info("");
		log.info("Specify \"-print <Printer Name>\" instead of \"-oxxx outputfile\" in order to print the output");
		log.info("");
		log.info("Configuration:");
		log.info(" fopConfigFile  - configuration file for Apache FOP");
		log.info(" convConfigFile - configuration file for this converter");
		log.info(" if not specified, configuration files will be taken from ./conf directory");
		log.info("");
		log.info("Specify file encoding in order to handle file text contents properly,\nfor example \"java -Dfile.encoding=Cp1251 -jar ...\"");
		log.info("");
		log.info("Specify time zone in order to pring date/time of a report in local time zone,\nfor example \"java -Duser.timezone=GMT+03 -jar ...\"");
		log.info("");
	}

	private boolean validateArgs(CommandLine commandLine) throws InstantiationException, IllegalAccessException {
		Command lastCommand = null;

		commandLine.reset();
		while (commandLine.hasArg()) {
			if ("-fc".equalsIgnoreCase(commandLine.getArg())) {
				fopConfigFile = commandLine.getNextArg();
			} else if ("-cc".equalsIgnoreCase(commandLine.getArg())) {
				convConfigFile = commandLine.getNextArg();
			} else if ("-ifr".equalsIgnoreCase(commandLine.getArg())) {
				ConvertReportToXML command = (ConvertReportToXML) createNewCommand(ConvertReportToXMLImpl.class, TwofoldCommand.class, lastCommand, true);
				command.setReportReaderClassName(FreeReportReader.class.getName());
				command.setInputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-ifr2217".equalsIgnoreCase(commandLine.getArg())) {
				ConvertReportToXML command = (ConvertReportToXML) createNewCommand(ConvertReportToXMLImpl.class, TwofoldCommand.class, lastCommand, true);
				command.setReportReaderClassName(FreeReportReader2217.class.getName());
				command.setInputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-ixml".equalsIgnoreCase(commandLine.getArg())) {
				ConvertReportToXML command = (ConvertReportToXML) createNewCommand(ConvertReportToXMLImpl.class, TwofoldCommand.class, lastCommand, true);
				command.setReportReaderClassName(XMLReportReader.class.getName());
				command.setInputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-ittf".equalsIgnoreCase(commandLine.getArg())) {
				ConvertFontToXML command = (ConvertFontToXML) createNewCommand(ConvertFontToXMLImpl.class, ConvertFontToXMLImpl.class, lastCommand, true);
				command.setInputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-oxml".equalsIgnoreCase(commandLine.getArg())) {
				Class<? extends CommandImpl> c = (lastCommand != null && lastCommand.getClass().equals(ConvertFontToXMLImpl.class) ? ConvertFontToXMLImpl.class
						: TwofoldCommand.class);
				CommandImpl command = (CommandImpl) createNewCommand(ConvertReportToXMLImpl.class, c, lastCommand, false);
				command.setOutputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-opdf".equalsIgnoreCase(commandLine.getArg())) {
				RenderXMLToOutput command = (RenderXMLToOutput) createNewCommand(RenderXMLToOutputImpl.class, TwofoldCommand.class, lastCommand, false);
				command.setOutputFormat(MimeConstants.MIME_PDF);
				command.setOutputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-opcl".equalsIgnoreCase(commandLine.getArg())) {
				RenderXMLToOutput command = (RenderXMLToOutput) createNewCommand(RenderXMLToOutputImpl.class, TwofoldCommand.class, lastCommand, false);
				command.setOutputFormat(MimeConstants.MIME_PCL);
				command.setOutputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-ops".equalsIgnoreCase(commandLine.getArg())) {
				RenderXMLToOutput command = (RenderXMLToOutput) createNewCommand(RenderXMLToOutputImpl.class, TwofoldCommand.class, lastCommand, false);
				command.setOutputFormat(MimeConstants.MIME_POSTSCRIPT);
				command.setOutputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-opng".equalsIgnoreCase(commandLine.getArg())) {
				RenderXMLToOutput command = (RenderXMLToOutput) createNewCommand(RenderXMLToOutputImpl.class, TwofoldCommand.class, lastCommand, false);
				command.setOutputFormat(MimeConstants.MIME_PNG);
				command.setOutputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-ofo".equalsIgnoreCase(commandLine.getArg())) {
				RenderXMLToOutput command = (RenderXMLToOutput) createNewCommand(RenderXMLToOutputImpl.class, TwofoldCommand.class, lastCommand, false);
				command.setOutputFormat(MimeConstants.MIME_XSL_FO);
				command.setOutputFileName(commandLine.getNextArg());
				lastCommand = command;
			} else if ("-print".equalsIgnoreCase(commandLine.getArg())) {
				RenderXMLToOutput command = (RenderXMLToOutput) createNewCommand(RenderXMLToOutputImpl.class, TwofoldCommand.class, lastCommand, false);
				command.setOutputFormat(MimeConstants.MIME_FOP_PRINT);
				command.setOutputFileName(commandLine.getNextArg());
				lastCommand = command;
			}

			if (lastCommand != null && lastCommand.hasInput() && lastCommand.hasOutput()) {
				commands.add(lastCommand);
				lastCommand = null;
			}

			commandLine.next();
		}

		return commands.size() > 0;
	}

	@SuppressWarnings("unchecked")
	private <T extends CommandImpl, V extends CommandImpl> CommandImpl createNewCommand(Class<T> commandClass, Class<V> spareCommandClass, Command lastCommand,
			boolean checkInput) throws InstantiationException, IllegalAccessException {
		if (lastCommand == null) {
			return commandClass.newInstance();
		}

		if (!lastCommand.getClass().equals(commandClass)) {
			CommandImpl cmd = spareCommandClass.newInstance();
			lastCommand.copy(cmd);
			return cmd;
		}

		if (checkInput ? lastCommand.hasInput() : lastCommand.hasOutput()) {
			return commandClass.newInstance();
		} else {
			return (T) lastCommand;
		}
	}
}
