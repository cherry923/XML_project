import java.io.IOException;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
    private static String uri;

    public static void main(String argv[]) {
        int select = 0;
        String nodeName = "";
        if (argv.length == 0) {
            System.err.println("Usage: java DOMTraverseuri ...");
            System.exit(1);
        }
        uri = argv[0];
        XMLManager xmlManager = new XMLManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1.load 2.make 3.find 4.save 5.traverse 6.insert 7.update 8.delete 9.exit");
            System.out.println("메뉴를 선택해주세요:");
            String selectedMenu = sc.nextLine();

            switch (selectedMenu) {
                case "1":
                    xmlManager.load(uri);
                    break;

                case "2":
                    if (xmlManager.getRoot() != null) {
                        System.out.println("기존 XML을 저장할 파일명을 입력해주세요");
                        String saveName = sc.nextLine();
                        xmlManager.save(saveName);
                    }
                    System.out.println("새로 생성할 파일명을 입력해주세요(확장자:.xml)");
                    String fileName = sc.nextLine();

                    System.out.println("루트 노드 이름을 입력해주세요.");
                    String rootName = sc.nextLine();

                    xmlManager.make(rootName);
                    break;

                case "3":
                    System.out.println("검색할 노드이름을 입력해주세요");
                    String name = sc.nextLine();
                    Node root = xmlManager.getRoot();
                    xmlManager.getSelectedNodes().clear();
                    xmlManager.find(root, name);
                    if (xmlManager.getSelectedNodes().isEmpty()) {
                        System.out.println("검색된 노드가 없습니다.");
                        break;
                    }
                    break;
                case "4":
                    System.out.println("저장할 파일명을 입력해주세요.");
                    fileName = sc.nextLine();
                    xmlManager.save(fileName);
                    break;
                case "5":
                    xmlManager.traverse(xmlManager.getRoot(), " ");
                    break;
                case "6":
                    System.out.println("추가할 노드의 부모노드 이름을 입력해주세요");
                    String parentNodeName = sc.nextLine();

                    xmlManager.getSelectedNodes().clear();

                    root = xmlManager.getRoot();

                    if (root.getNodeName().equals(parentNodeName)) {
                        xmlManager.getSelectedNodes().add(root);
                        for (int i = 0; i < xmlManager.getSelectedNodes().size(); i++) {
                            System.out.println(i + 1 + "." + xmlManager.getSelectedNodes().get(i).getNodeName());
                        }
                    } else {
                        xmlManager.find(root, parentNodeName);
                    }

                    if (xmlManager.getSelectedNodes().isEmpty()) {
                        System.out.println("검색된 노드가 없습니다.");
                        break;
                    }
                    System.out.println("몇번째 노드의 추가하시겠습니까?");
                    int nodeIdx = Integer.parseInt(sc.nextLine());

                    System.out.println("추가할 타입을 입력해주세요. 1.ELEMENT 2.ATTRIBUTE 3.TEXT 4.COMMENT");
                    String type = sc.nextLine();

                    System.out.println("추가할 노드의 이름을 입력해주세요");
                    nodeName = sc.nextLine();

                    List<Node> selectedNodes = xmlManager.getSelectedNodes();
                    Node selectedNode = selectedNodes.get(nodeIdx - 1);

                    NodeList children = selectedNode.getChildNodes();
                    if (children.getLength() == 0) {
                        System.out.println("해당 부모노드의 자식이 존재하지않아 첫 번째 자식으로 추가합니다.");
                        xmlManager.insert(parentNodeName, nodeIdx, type, nodeName, 0);
                    } else {
                        for (int i = 0; i < children.getLength(); i++) {
                            Node child = children.item(i);
                            System.out.println(i + 1 + "." + child.getNodeName());
                        }

                        System.out.println("위치를 입력해주세요(입력된 위치의 앞부분에 추가됩니다.)");
                        select = Integer.parseInt(sc.nextLine());

                        xmlManager.insert(parentNodeName, nodeIdx, type, nodeName, select - 1);
                    }
                    break;
                case "7":
                    System.out.println("수정할 노드의 이름을 입력해주세요");
                    nodeName = sc.nextLine();

                    xmlManager.getSelectedNodes().clear();
                    xmlManager.find(xmlManager.getRoot(), nodeName);

                    if (xmlManager.getSelectedNodes().isEmpty()) {
                        System.out.println("검색된 노드가 없습니다.");
                        break;
                    }

                    System.out.println("몇 번째 노드를 수정하시겠습니까?");
                    select = Integer.parseInt(sc.nextLine());

                    if (select < 1 || select > xmlManager.getSelectedNodes().size()) {
                        System.out.println("잘못된 노드 번호입니다.");
                        break;
                    }

                    System.out.println("수정할 타입을 선택해주세요.");
                    System.out.println("1.ELEMENT 이름 2.ATTRIBUTE 값 3.TEXT 값 4.COMMENT 값");
                    String updateType = sc.nextLine();

                    String updateName = "";
                    String updateValue = "";

                    if (updateType.equals("1")) {
                        System.out.println("변경할 Element 이름을 입력해주세요");
                        updateName = sc.nextLine();

                    } // element만 일단 구현

                    xmlManager.update(select, updateType, updateName, updateValue);
                    break;
                case "8":
                    System.out.println("삭제할 노드의 이름을 입력해주세요");
                    nodeName = sc.nextLine();
                    xmlManager.getSelectedNodes().clear();
                    xmlManager.find(xmlManager.getRoot(), nodeName);

                    if (xmlManager.getSelectedNodes().isEmpty()) {
                        System.out.println("검색된 노드가 없습니다.");
                        break;
                    }
                    System.out.println("몇 번째 노드를 삭제하시겠습니까?");
                    select = Integer.parseInt(sc.nextLine());

                    if (select < 1 || select > xmlManager.getSelectedNodes().size()) {
                        System.out.println("잘못된 노드 번호입니다.");
                        return;
                    }

                    xmlManager.delete(select);
                    break;
                case "9":
                    return;

            }
        }
    }
}