package com.ityu.elec.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import com.ityu.elec.domain.ElecFileUpload;

public class LuceneUtils {

	public static void addIndex(ElecFileUpload fileUpload){
		
		Document document = FileUploadDocument.FileUploadToDocument(fileUpload);
		try{
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36,Configuration.getAnalyzer());
			IndexWriter indexWriter = new IndexWriter(Configuration.getDirectory(),indexWriterConfig);
			indexWriter.addDocument(document);
			indexWriter.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**使用ID，删除索引库*/
	public static void deleteIndex(Integer seqId){
		//使用词条删除
		String id = NumericUtils.intToPrefixCoded(seqId);
		Term term = new Term("seqId",id);
		/**新增、修改、删除、查询都会使用分词器*/
		try{
			IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_36,Configuration.getAnalyzer());
			IndexWriter indexWriter = new IndexWriter(Configuration.getDirectory(),writerConfig);
			indexWriter.deleteDocuments(term);
			indexWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static List<ElecFileUpload> searcherIndexByCondition(String projId,String belongTo,String queryString){
		List<ElecFileUpload> fileUploadList = new ArrayList<ElecFileUpload>();
		try {
			IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(Configuration.getDirectory()));
			BooleanQuery query = new BooleanQuery();
			//条件一（所属单位）
			if(StringUtils.isNotBlank(projId)) {
				TermQuery query1 = new TermQuery(new Term("projId",projId));
				query.add(query1,Occur.MUST);//相当于sql语句的and
			}
			if(StringUtils.isNotBlank(belongTo)){
				TermQuery query2 = new TermQuery(new Term("belongTo",belongTo));
				query.add(query2,Occur.MUST);
			}
			if(StringUtils.isNotBlank(queryString)){
				QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36, new String[]{"fileName","comment"}, Configuration.getAnalyzer());
				Query query3 = queryParser.parse(queryString);
				query.add(query3,Occur.MUST);
			}
			TopDocs topDocs = indexSearcher.search(query, 100);
			System.out.println("查询的总记录数"+topDocs.totalHits);
			//表示返回的结果集
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'><b>","</b></font>");
			Scorer scorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(formatter,scorer);
			
			int fragmentSize =50;
			Fragmenter fragmenter = new SimpleFragmenter();
			highlighter.setTextFragmenter(fragmenter);
			if(scoreDocs!=null && scoreDocs.length>0){
				for (ScoreDoc scoreDoc : scoreDocs) {
					System.out.println("相关度得分："+scoreDoc.score);
					int doc = scoreDoc.doc;
					Document document = indexSearcher.doc(doc);
					String fileName = highlighter.getBestFragment(Configuration.getAnalyzer(), "fileName",document.get("fileName"));
					if(StringUtils.isNotBlank(fileName)){
						 fileName = document.get("fileName");
						 if(fileName!=null && fileName.length()>fragmentSize){
							 fileName = fileName.substring(0, fragmentSize);
						 }
					}
					document.getField("fileName").setValue(fileName);
					String comment = highlighter.getBestFragment(Configuration.getAnalyzer(),"comment",document.get("comment"));
					if(StringUtils.isBlank(comment)){
						comment = document.get("comment");
						if(comment!=null && comment.length()>fragmentSize){
							comment = comment.substring(0,fragmentSize);
							
						}
					}
					document.getField("comment").setValue(comment);
					
					ElecFileUpload elecFileUpload = FileUploadDocument.documentToFileUpload(document);
					
					fileUploadList.add(elecFileUpload);
				}
			}
			indexSearcher.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileUploadList;
		
	}
}
