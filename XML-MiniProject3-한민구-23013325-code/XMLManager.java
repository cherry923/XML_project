import org.xml.sax.SAXException;
import java.util.*;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSOutput;
import java.io.FileOutputStream;

public class XMLManager {
    // private NodeList nodeList;
    private Document doc = null;
    private Node root;
    private List<Node> selectedNodes = new ArrayList<>();
    private static boolean setValidation = false;

    public void load(String uri) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(uri);
            root = doc.getDocumentElement();

            selectedNodes.clear();

            System.out.println("load성공 하였습니다.");

        } catch (FactoryConfigurationError e) {// unable to get a document builder factorye.printStackTrace(System.err);
        } catch (ParserConfigurationException e) {// parser was unable to be configurede.printStackTrace(System.err);
        } catch (SAXException e) {
            // parsing error
            e.printStackTrace(System.err);
        } catch (IOException e) {
            // i/o error
            e.printStackTrace(System.err);
        }
    }

    public void make(String rootName) { // validation 안해도 됨?
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            doc = builder.newDocument();

            root = doc.createElement(rootName);
            doc.appendChild(root);

            selectedNodes.clear();

        } catch (Exception e) {

        }
    }

    public void find(Node node, String nodeName) {

        if (node == null) {
            System.out.println("load된 xml파일이 존재하지 않습니다.");
            return;
        }
        if (node.getNodeName().equals(nodeName)) {
            System.out.print("[" + getDepth(node) + ", ");
            System.out.print(getSiblingIndex(node) + "] ");
            System.out.println(node.getNodeName());
            selectedNodes.add(node);
        }
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            find(child, nodeName);
        }
    }

    public void save(String fileName) {

        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");

            LSSerializer serializer = impl.createLSSerializer();
            serializer.getDomConfig().setParameter("format-pretty-print", true);
            LSOutput output = impl.createLSOutput();

            FileOutputStream fos = new FileOutputStream(fileName);
            output.setByteStream(fos);

            serializer.write(doc, output);
            fos.close();

            System.out.println("XML 저장 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * public void traverse(Node node) {
     * 
     * NodeList children = node.getChildNodes();
     * System.out.println();
     * 
     * for (int i = 0; i < children.getLength(); i++) {
     * Node child = children.item(i);
     * int type = child.getNodeType();
     * switch (type) {
     * case Node.DOCUMENT_NODE:
     * System.out.println("[Document] " + child.getNodeName());
     * break;
     * 
     * case Node.ENTITY_NODE:
     * System.out.println("[ENTITY] " + child.getNodeName());
     * break;
     * 
     * case Node.ELEMENT_NODE:
     * System.out.println("[Element] " + child.getNodeName());
     * 
     * if (child.hasAttributes()) {
     * NamedNodeMap childAttributes = child.getAttributes();
     * for (int j = 0; j < childAttributes.getLength(); j++) {
     * Node attribute = childAttributes.item(j);
     * System.out.print("[ATTRIBUTE] ");
     * System.out.println(attribute.getNodeName() + ": " + attribute.getNodeValue()
     * + " ");
     * 
     * }
     * }
     * break;
     * 
     * case Node.ENTITY_REFERENCE_NODE:
     * System.out.print("[ENTITY_REFERENCE] " + child.getNodeName());
     * break;
     * 
     * case Node.CDATA_SECTION_NODE:
     * System.out.print("[CDATA_SECTION] ");
     * System.out.print(child.getNodeName());
     * System.out.println("  " + child.getNodeValue());
     * break;
     * 
     * case Node.COMMENT_NODE:
     * System.out.print("[COMMENT] ");
     * System.out.print(child.getNodeName());
     * System.out.println("  " + child.getNodeValue());
     * break;
     * 
     * /*
     * case Node.TEXT_NODE:
     * System.out.print("[TEXT] ");
     * System.out.print(child.getNodeName());
     * System.out.println("  " + child.getNodeValue());
     * break;
     * }
     * 
     * traverse(child);
     * }
     */

    public void traverse(Node node, String indent) {
        if (node == null)
            return;

        int type = node.getNodeType();
        switch (type) {
            case Node.DOCUMENT_NODE:
                System.out.println(indent + "[Document] " + node.getNodeName());
                break;

            case Node.ENTITY_NODE:
                System.out.println(indent + "[ENTITY] " + node.getNodeName());
                break;

            case Node.ELEMENT_NODE:
                System.out.print(indent + "[Element] " + node.getNodeName() + " ");
                if (node.hasAttributes()) {
                    NamedNodeMap childAttributes = node.getAttributes();
                    for (int j = 0; j < childAttributes.getLength(); j++) {
                        Node attribute = childAttributes.item(j);
                        System.out.print("[ATTRIBUTE] ");
                        System.out.print(attribute.getNodeName() + ": " + attribute.getNodeValue()
                                + " ");
                    }
                }
                System.out.println();
                break;

            case Node.ENTITY_REFERENCE_NODE:
                System.out.print(indent + "[ENTITY_REFERENCE] " + node.getNodeName());
                break;

            case Node.CDATA_SECTION_NODE:
                System.out.print(indent + "[CDATA_SECTION] ");
                System.out.print(node.getNodeName());
                System.out.println("  " + node.getNodeValue());
                break;

            case Node.COMMENT_NODE:
                System.out.print(indent + "[COMMENT] ");
                System.out.print(node.getNodeName());
                System.out.println("  " + node.getNodeValue());
                break;

            case Node.TEXT_NODE:
                System.out.print(indent + "[TEXT] ");
                System.out.print(node.getNodeName());
                System.out.println("  " + node.getNodeValue());
                break;
        }

        NodeList children = node.getChildNodes();
        if (children != null) {
            int len = children.getLength();
            for (int i = 0; i < len; i++)
                traverse(children.item(i), indent + "   ");
        }
    }

    public void insert(String parentNodeName, int nodeIdx, String type, String nodeName, int select, String nodeValue) {

        Node parent = selectedNodes.get(nodeIdx - 1);

        NodeList children = parent.getChildNodes();
        Node newNode;

        Node reference = children.item(select);
        Node textNode = doc.createTextNode("\n ");
        ;

        switch (type) {

            case "1": // element 노드
                newNode = doc.createElement(nodeName);
                newNode.appendChild(doc.createTextNode(nodeValue));

                reference = children.item(select);
                textNode = doc.createTextNode("\n ");

                parent.insertBefore(textNode, reference);
                parent.insertBefore(newNode, reference);
                System.out.println();
                break;

            case "2": // attribute 노드
                Element element = (Element) parent;
                element.setAttribute(nodeName, nodeValue);
                break;

            case "3": // text 노드
                newNode = doc.createTextNode(nodeValue);

                parent.insertBefore(textNode, reference);
                parent.insertBefore(newNode, reference);
                break;
            case "4": // comment 노드
                newNode = doc.createComment(nodeValue);
                parent.insertBefore(textNode, reference);
                parent.insertBefore(newNode, reference);
                break;

        }

        children = parent.getChildNodes();
        Node selectedNode = children.item(select);
        String outName = selectedNode.getNodeName();
        System.out.println("item(" + select + "):" + outName);

    }

    public void update(int select, String type, String name, String value) {

        if (select < 1 || select > selectedNodes.size()) {
            System.out.println("잘못된 노드 번호입니다.");
            return;
        }

        Node selectedNode = selectedNodes.get(select - 1);
        Node parent = selectedNode.getParentNode();

        if (parent == null || selectedNode.getNodeType() != Node.ELEMENT_NODE) {
            System.out.println("수정할 수 없는 노드입니다.");
            return;
        }

        Element newNode = doc.createElement(name);
        newNode.setTextContent(value);

        parent.insertBefore(newNode, selectedNode);
        parent.removeChild(selectedNode);

        System.out.println("수정 완료");
    }

    public void delete(int select) {

        Node selectedNode = selectedNodes.get(select - 1);
        Node parent = selectedNode.getParentNode();

        Node previousNode = selectedNode.getPreviousSibling();

        if (previousNode != null &&
                previousNode.getNodeType() == Node.TEXT_NODE) {

            parent.removeChild(previousNode);
        } // #text 노드 삭제

        parent.removeChild(selectedNode);
        selectedNodes.remove(select - 1);

        String outName = selectedNode.getNodeName();
        System.out.println("item(" + select + ")=" + outName + "가 삭제 되었습니다.");
    }

    public int getDepth(Node node) {
        int index = 0;
        while ((node = node.getParentNode()) != null)
            index++;
        return index;
    }

    protected int getSiblingIndex(Node node) {
        int index = 1;

        while ((node = node.getPreviousSibling()) != null)
            if (node.getNodeType() != Node.TEXT_NODE
                    && node.getNodeType() != Node.COMMENT_NODE)
                index++;

        return index;
    }

    Node getRoot() {
        if (doc == null) {
            return null;
        }
        return doc.getDocumentElement();
    }

    List<Node> getSelectedNodes() {
        return selectedNodes;
    }

    public Node addElement(Node parent, Node reference, String name, String value) {

        Element newElement = doc.createElement(name);
        newElement.setTextContent(value);

        parent.insertBefore(newElement, reference);

        return newElement;
    }

}