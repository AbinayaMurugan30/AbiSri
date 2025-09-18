const AdmZip = require('adm-zip');
const path = require('path');

function zipReportFolder(sourceFolder = './reports/html', zipName = 'TestReport.txt') {
  const zip = new AdmZip();
  zip.addLocalFolder(sourceFolder);

  const outputPath = path.resolve(`./reports/${zipName}`);
  zip.writeZip(outputPath);
  console.log(`✅ Zipped report saved to: ${outputPath}`);
  return outputPath;
}

module.exports = zipReportFolder;
