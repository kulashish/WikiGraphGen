package in.ac.iitb.wiki;

import in.ac.iitb.qassist.util.EntityParser;
import in.ac.iitb.qassist.util.InterpretationSplitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.StringEdgeNameProvider;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.graph.SimpleGraph;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.SemClassStore;
import com.aneedo.search.SemanticIndexSearcher;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.util.SemanticSearchUtil;

public class GraphGen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Invalid Usage");
			System.exit(1);
		}
		String rawQuery = args[0];
		SemanticIndexSearcher searcher = SemanticIndexSearcher.getInstance();
		// SemClassStore semclassStore = searcher.getResults(rawQuery, null);
		SemClassStore semClassStore = new SemClassStore();
		try {
			searcher.storeEntitiesMatchingQuery(IndexingConstants.PAGE_ID,
					rawQuery, semClassStore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Writer dotWriter = null;
		Graph<String, DefaultEdge> graph = null;
		try {
			graph = new GraphGen().generateGraph(semClassStore);
			dotWriter = new FileWriter(
					"/home/ashish/Documents/graphcode/indira.dot");
		} catch (IOException e) {
			e.printStackTrace();
		}
		new DOTExporter<String, DefaultEdge>(new IntegerNameProvider<String>(),
				new StringNameProvider<String>(),
				new StringEdgeNameProvider<DefaultEdge>()).export(dotWriter,
				graph);
	}

	public UndirectedGraph<String, DefaultEdge> generateGraph()
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				"/home/ashish/Documents/graphcode/out1.txt"));
		String line = null;
		String[] entities = null;
		UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(
				DefaultEdge.class);
		String title = "Indira Gandhi";
		graph.addVertex(title);
		while ((line = reader.readLine()) != null) {
			entities = new EntityParser(line.substring(line.indexOf(':') + 1))
					.getEntityNames();
			for (String entity : entities) {
				graph.addVertex(entity);
				graph.addEdge(title, entity);
			}
		}
		return graph;
	}

	public UndirectedGraph<String, DefaultEdge> generateGraph(
			SemClassStore semClassStore) {
		TIntObjectHashMap<SemEntityBean> semEntityBeanMap = semClassStore
				.getSemEntityBeanMap();

		TIntObjectIterator<SemEntityBean> iter = semEntityBeanMap.iterator();
		SemEntityBean semEntityBean = null;
		TIntObjectHashMap<List<String>> semClassMap = null;
		TIntObjectIterator<List<String>> semClassIter = null;
		UndirectedGraph<String, DefaultEdge> undirectedGraph = new SimpleGraph<String, DefaultEdge>(
				DefaultEdge.class);
		String mainTitle = null;

		while (iter.hasNext()) {
			iter.advance();
			semEntityBean = iter.value();
			mainTitle = semEntityBean.getTitle();
			undirectedGraph.addVertex(mainTitle);

			semClassMap = semEntityBean.getWordSemClassMap();
			semClassIter = semClassMap.iterator();
			String[] entityTitles = null;

			String semClassName = SemanticSearchUtil.getInstance()
					.getSemClassName(semClassIter.key());
			while (semClassIter.hasNext()) {
				semClassIter.advance();
				for (String entity : semClassIter.value()) {
					entityTitles = new EntityParser(entity).getEntityNames();
					for (String title : entityTitles) {
						undirectedGraph.addVertex(title);
						undirectedGraph.addEdge(mainTitle, title);
					}
				}
			}
		}
		return undirectedGraph;
	}
}