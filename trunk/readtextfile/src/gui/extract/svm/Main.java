package gui.extract.svm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import core.extract.svm.Header;
import core.extract.svm.Line;
import core.extract.svm.Word;

import utilily.extract.svm.HeaderReader;
import utilily.extract.svm.LabelConst;

public class Main {
	
	/**
	 * @param args
	 */
	public static final String separateLine = "\n";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String pathFile = "text.txt";
		
		String pathStopWordListFile = "StopWordList.txt";	
		ArrayList<String> stopWordList = HeaderReader.readTextFile(pathStopWordListFile);
		
		String pathFile = "tagged_headers.txt";		
		
		String[] headersText = HeaderReader.read(pathFile);
		Header[] headers = new Header[headersText.length];
		int countHeader = 0;
		for (int i = 0; i < headersText.length; i++) {
			headers[i] = new Header(headersText[i]);
			countHeader++;
		}
		
		StringBuilder affiliationString = new StringBuilder();
		StringBuilder addressString = new StringBuilder();
		
		System.out.println("Counted Header : " + countHeader);
		
		for (int i = 0; i < 500; i++) {
			//Line Affiliation
			ArrayList<Line> linesAffiliation = headers[i].getLineWithLabel(LabelConst.AFFILIATION);
			
			for (Line line : linesAffiliation) {
				affiliationString.append(line.getContent());
			}
			
			ArrayList<Line> linesAddress = headers[i].getLineWithLabel(LabelConst.ADDRESS);
			for (Line line : linesAddress) {
				addressString.append(line.getContent());
			}
		}
		
		System.out.println(affiliationString.toString());
		
		// Statistic Affiliation word list
		
		String affiliationRemoved = affiliationString.toString().replaceAll(",", "");		
		String[] affiTokens = affiliationRemoved.split(" ");
		ArrayList<Word> affiliationListWord = new ArrayList<Word>();
		for (int i = 0; i < affiTokens.length; i++) {
			if(!affiTokens[i].trim().equals("")){
				
				//check stop word
				boolean isStopWord = false;
				for(int j = 0; j < stopWordList.size(); j++){
					if(affiTokens[i].trim().toLowerCase().equals(stopWordList.get(j).trim())){
						isStopWord = true;
						j = stopWordList.size();
					}
				}
				
				if(isStopWord == false){
					Word word = new Word(affiTokens[i].trim().toLowerCase());	
					boolean existedWord = false;
					for (Word wordlist : affiliationListWord) {
						if (word.getContent().equals(wordlist.getContent())) {
							existedWord = true;
							wordlist.increaseDFValue();
						}
					}
					if(existedWord == false){	
						affiliationListWord.add(word);
					}
				}
			}
		}
		
		String addressRemoved = addressString.toString().replaceAll(",", "");		
		String[] addressTokens = addressRemoved.split(" ");
		ArrayList<Word> addressListWord = new ArrayList<Word>();
		for (int i = 0; i < addressTokens.length; i++) {
			if(!addressTokens[i].trim().equals("")){
				
				//check stop word
				boolean isStopWord = false;
				for(int j = 0; j < stopWordList.size(); j++){
					if(addressTokens[i].trim().toLowerCase().equals(stopWordList.get(j).trim())){
						isStopWord = true;
						j = stopWordList.size();
					}
				}
				
				if(isStopWord == false){
					Word word = new Word(addressTokens[i].trim().toLowerCase());	
					boolean existedWord = false;
					for (Word wordlist : addressListWord) {
						if (word.getContent().equals(wordlist.getContent())) {
							existedWord = true;
							wordlist.increaseDFValue();
						}
					}
					if(existedWord == false){	
						addressListWord.add(word);
					}
				}
			}
		}
		
		
		//sort affiliation word list data largest to 10		
		for (int i = 0; i < affiliationListWord.size() - 1; i++) {			
			for (int j = i; j < affiliationListWord.size(); j++) {
				if(affiliationListWord.get(i).getDfValue() < affiliationListWord.get(j).getDfValue()){
					Word wordTemp = affiliationListWord.get(i);
					affiliationListWord.set(i, affiliationListWord.get(j));
					affiliationListWord.set(j, wordTemp);
				}
			}
		}
		
		for (int i = 0; i < addressListWord.size() - 1; i++) {			
			for (int j = i; j < addressListWord.size(); j++) {
				if(addressListWord.get(i).getDfValue() < addressListWord.get(j).getDfValue()){
					Word wordTemp = addressListWord.get(i);
					addressListWord.set(i, addressListWord.get(j));
					addressListWord.set(j, wordTemp);
				}
			}
		}
		
		for (int i = 0; i < 20; i++) {			
			System.out.println(affiliationListWord.get(i).getContent() + " : " + affiliationListWord.get(i).getDfValue());
		}
		
	}
}