package ru.arcadoss.collagemaker.json;

/**
 * Created by arcturus at 04.07.14
 * {"meta":{"error_type":"APINotAllowedError","code":400,"error_message":"you cannot view this resource"}}
 */
public class MetaInfo {
	public int code;
	public String errorMessage;
	public ErrorType errorType;
}
