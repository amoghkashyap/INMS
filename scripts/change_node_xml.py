import xml.etree.ElementTree as ET
import glob

for xml_file in glob.glob('./*.xml'):
    tree = ET.parse(xml_file)
    root = tree.getroot()
    for disc in root.iter('name'):
        print disc.text
        disc.text = 'egg'
        print disc.text
        tree.write(xml_file)
