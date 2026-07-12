import sys
sys.stdout.reconfigure(encoding='utf-8')
from pypdf import PdfReader

files = [
    r'D:\zhangfuzeng\code\Java-Notes\AI\oceanbase\OceanBase湖库一体的 AI 数据库.pdf',
    r'D:\zhangfuzeng\code\Java-Notes\AI\oceanbase\OceanBase面向 AI 的全新产品家族.pdf',
    r'D:\zhangfuzeng\code\Java-Notes\AI\oceanbase\OceanBase：AI 时代数据库的变与不变.pdf'
]

for f in files:
    sep = '=' * 80
    print(f'\n{sep}')
    print(f'FILE: {f}')
    print(f'{sep}')
    reader = PdfReader(f)
    for i, page in enumerate(reader.pages[:40]):
        text = page.extract_text()
        if text.strip():
            print(f'\n--- PAGE {i+1} ---')
            print(text[:3000])
