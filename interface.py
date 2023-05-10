from flask import Flask, render_template, request
import zipfile
import os
from flask import send_file
import glob
import shutil


app = Flask(__name__)


@app.post('/driver')
def post_driver():
    tmpFiles = glob.glob('./tmp/*')
    for f in tmpFiles:
        os.system('rm -r '+f)
    name = request.form['name']
    f = request.files['file']
    file_object = f.stream._file
    with zipfile.ZipFile(file_object, 'r') as zip_ref:
        zip_ref.extractall("./tmp")
    files = os.listdir('./tmp/')
    stFile = [file_name for file_name in files if file_name.endswith(".st")]
    os.system('java -jar ./openapi-generator-cli.jar generate -i ./tmp/schema.yaml -g scala-sttp --skip-validate-spec -o ./tmp --additional-properties modelPropertyNaming=original')
    os.system('rm -r ./tmp/.openapi-generator/')
    os.system('rm ./tmp/.openapi-generator-ignore')
    os.system('rm ./tmp/build.sbt')
    os.system('rm ./tmp/README.md')
    os.system('rm -r ./tmp/project/')
    os.system('mv ./tmp/src/main/scala/org/openapitools/client/api/ ./tmp')
    os.system('mkdir ./tmp/api')
    apiFiles = glob.glob('./tmp/api/*')
    for f in apiFiles:
        f_obj = open(f, "r")
        contents = f_obj.read().replace("package org.openapitools.client.api", "package examples."+name+".api").replace("import org.openapitools.client", "import examples."+name)
        contents = contents.replace("import examples."+name+".model.AnyType\n", "")
        f_obj.close()
        f_obj = open(f, "w")
        f_obj.write(contents)
    os.system('mv ./tmp/src/main/scala/org/openapitools/client/core/ ./tmp')
    os.system('mkdir ./tmp/core')
    coreFiles = glob.glob('./tmp/core/*')
    for f in coreFiles:
        f_obj = open(f, "r")
        contents = f_obj.read().replace("package org.openapitools.client.core", "package examples."+name+".core").replace("import org.openapitools.client", "import examples."+name)
        f_obj.close()
        f_obj = open(f, "w")
        f_obj.write(contents)
    os.system('mv ./tmp/src/main/scala/org/openapitools/client/model/ ./tmp')
    os.system('mkdir ./tmp/model')
    coreFiles = glob.glob('./tmp/core/*')
    for f in coreFiles:
        f_obj = open(f, "r")
        contents = f_obj.read().replace("package org.openapitools.client.model", "package examples."+name+".model").replace("import org.openapitools.client", "import examples."+name)
        f_obj.close()
        f_obj = open(f, "w")
        f_obj.write(contents)
    os.system('rm -r ./tmp/src/')
    os.system('java -jar ./driver-assembly-0.0.3.jar ./tmp/ ./tmp/'+stFile[0]+' ./tmp/preamble.txt false true')
    shutil.make_archive('./out/'+name+'_driver', 'zip', './tmp/')
    return send_file('./out/'+name+'_driver.zip', as_attachment=True)
