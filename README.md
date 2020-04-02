Standardised codes from Recommendation 20, mantained by UNECE.

## Data

Data comes from the Excel file, merging Annex I and Annex II/III. It doesn't include units marked as deprecated and removed.

## Preparation

* levels.csv is derived from the latest pdf available (Section VII) and explains the categories of the main dataset
* From the Excel spreadsheet we have merged the two Annexes to have the sector and the codes in the same file, removing the symbol and conversion factor columns; entries marked as deprecated (D) or deleted (X) have been removed.

## Usage via Jitpack
Jitpack is a service that packages github repositories on-the-fly when you request an archive for the first time and the caches the result.

### JVM / Android
The package can be used as a compile-time dependency for JVM / Andriod projects through https://jitpack.io. 
1. Add the https://jitpack.io repository to your build (Maven, Gradle, ...)
2. Add the dependency `com.github.datasets:unece-units-of-measure:<name-of-the-tag>` \
Example: `com.github.datasets:unece-units-of-measure:rec20_Rev13e_2017`

See the [jitpack documentation](https://jitpack.io/docs/) for further details.

### Zip download
Because .jar files are in essence just normal .zip files, it is perfectly possible to use the jitpack artifact as a zip file source for non-JVM usage. 

URL pattern: ht<span>tps:</span>//jitpack.io/com/github/datasets/unece-units-of-measure/<name-of-the-tag>/unece-units-of-measure-<name-of-the-tag>.jar \
URL example: https://jitpack.io/com/github/datasets/unece-units-of-measure/rec20_Rev13e_2017/unece-units-of-measure-rec20_Rev13e_2017.jar

## License

All data is licensed under the [ODC Public Domain Dedication and Licence (PDDL)](http://opendatacommons.org/licenses/pddl/1-0/).
