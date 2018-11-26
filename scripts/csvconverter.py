import os
import glob
import pandas as pd
import xml.etree.ElementTree as ET
import sys

path = sys.argv[1]
class_number = sys.argv[2]
folder_name = sys.argv[3]
output_name = sys.argv[4]

def xml_to_csv(path):
    xml_list = []
    for xml_file in glob.glob(path + '/*.xml'):
        tree = ET.parse(xml_file)
        root = tree.getroot()
        for member in root.findall('object'):
            value = (path + root.find('filename').text,
                     int(member[4][0].text),
                     int(member[4][1].text),
                     int(member[4][2].text),
                     int(member[4][3].text),
                     class_number
                     )
            xml_list.append(value)
    column_name = ['filename', 'xmin', 'ymin', 'xmax', 'ymax', 'class']
    xml_df = pd.DataFrame(xml_list, columns=column_name)
    return xml_df


def main():
    image_path = os.path.join(os.getcwd(), folder_name)
    xml_df = xml_to_csv(image_path)
    xml_df.to_csv(output_name, index=None)
    print('Successfully converted xml to csv.')


main()

