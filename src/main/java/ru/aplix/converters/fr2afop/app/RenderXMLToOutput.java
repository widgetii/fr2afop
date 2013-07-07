package ru.aplix.converters.fr2afop.app;

public interface RenderXMLToOutput extends Command {

	public void setOutputFormat(String outputFormat);

	public void setFopConfigFileName(String configFileName);
}
