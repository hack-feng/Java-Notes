import sys
sys.stdout.reconfigure(encoding='utf-8')
from docx import Document

doc = Document(r'D:\zhangfuzeng\code\Java-Notes\AI\oceanbase\【速记】AI时代数据库的新答案.docx')
print('=' * 80)
print('FILE: 【速记】AI时代数据库的新答案.docx')
print('=' * 80)

for para in doc.paragraphs:
    if para.text.strip():
        print(para.text)
