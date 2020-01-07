package it.kruczek.ztm.Processing;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

class ConvertContentToXmlDocument extends DoFn<String, Document> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

//        System.out.println(c.element().toString());
        System.exit(123);
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(c.element().toString())));
            c.output(doc);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}


//class ConvertContentToXmlDocumentTransformer extends PTransform<PCollection<KV<String, String>>, PCollection<Document>> {
//    @Override
//    public PCollection<Document> expand(PCollection<KV<String, String>> files) {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder;
//
////        return files
////                .apply()
//
//        try {
//            builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new InputSource(new StringReader(c.element().toString())));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

class GetAllPositionsToJson extends DoFn<Document, JSONObject> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        Document doc = c.element();
        Iterable<Node> elements = convertToIterable(doc.getElementsByTagName("p"));

        for (Node element : elements) {
            c.output(new JSONObject(element.getNodeValue().toString()));
        }
    }

    private Iterable<Node> convertToIterable(final NodeList n) {
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {

                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < n.getLength();
                    }

                    @Override
                    public Node next() {
                        if (hasNext()) {
                            return n.item(index++);
                        } else {
                            throw new NoSuchElementException();
                        }
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}

class GetAllPositions extends DoFn<KV<String, String>, String> {
    @ProcessElement
    public void processElement(ProcessContext c) {

        if (c.element().getValue() == null) {
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        PositionFactory positionFactory = new PositionFactory();

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(c.element().getValue())));
            NodeList elements = doc.getElementsByTagName("p");

            for (int i = 0; i < elements.getLength(); i++) {
                JSONObject position = positionFactory.create(c.element().getKey(), elements.item(i));
                c.output(position.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class ParseXmlFiles extends PTransform<PCollection<KV<String, String>>, PCollection<String>> {

    @Override
    public PCollection<String> expand(PCollection<KV<String, String>> files) {
        return
                files
//                        .apply(MapElements.into(TypeDescriptors.strings()).via((KV<String, String> ob) -> {
//                            return ob.getValue();
//                        }))
                        .apply(ParDo.of(new GetAllPositions()))
//                        .setCoder(SerializableCoder.of(Document.class))
//                        .apply(ParDo.of(new GetAllPositionsToJson()))
//                        .apply(MapElements.into(TypeDescriptors.strings()).via((String ob) -> {
//                            System.out.println(ob);
//                            return ob;
//                        }))
//                        .setCoder(StringUtf8Coder.of())
                ;
    }

    static public ParseXmlFiles create() {
        return new ParseXmlFiles();
    }
}
