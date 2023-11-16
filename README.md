[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.10145076.svg)](https://doi.org/10.5281/zenodo.10145076)

# MultiDendrograms

A hierarchical clustering tool.


## Description

**MultiDendrograms** is a simple yet powerful program to make the Hierarchical Clustering of real data, distributed under an Open Source license. Starting from a distances (or similarities) matrix, **MultiDendrograms** calculates its dendrogram using the most common Agglomerative Hierarchical Clustering algorithms, allows the tuning of many of the graphical representation parameters, and the results may be easily exported to file. A summary of characteristics:

- Multiplatform: developed in Java, runs in all operating systems (e.g. Windows, Linux and MacOS).

- Graphical user interface: data selection, hierarchical clustering options, dendrogram representation parameters, navigation across the dendrogram, deviation measures.

- Hierarchical Clustering algorithms: weighted and unweighted variable-group versions of Single Linkage, Complete Linkage, Arithmetic Linkage (UPGMA), Versatile Linkage, Centroid, Ward and Beta Flexible.

- Representation parameters: size, orientation, labels, axis, etc.

- Dendrogram measures: Tree Balance, Cophenetic Correlation Coefficient, Normalized Mean Squared Error, Normalized Mean Absolute Error and Space Distortion.

- Export: ultrametric matrix, dendrogram measures, dendrogram details in text, Newick and JSON tree formats.

- Plot: dendrogram image in JPG, PNG and EPS formats.

- Command-line: available direct calculation of hierarchical clustering from the command-line, without the need to use the graphical interface.

**MultiDendrograms** implements the variable-group algorithms in [[1](#references)] to solve the non-uniqueness problem found in the standard pair-group algorithms and implementations. This problem arises when two or more minimum distances between different clusters are equal during the amalgamation process. The standard approach consists in choosing a pair, breaking the ties between distances, and proceeds in the same way until the final hierarchical classification is obtained. However, different clusterings are possible depending on the criterion used to break the ties (usually a pair is just chosen at random!), and the user is unaware of this problem.

The variable-group algorithms group more than two clusters at the same time when ties occur, given rise to a graphical representation called multidendrogram. Their main properties are:

- When there are no ties, the variable-group algorithms give the same results as the pair-group ones.

- They always give a uniquely determined solution.

- In the multidendrogram representation for the results one can explicitly observe the occurrence of ties during the agglomerative process. Furthermore, the height of any fusion interval (the bands in the program) indicates the degree of heterogeneity inside the corresponding cluster.

**MultiDendrograms** also introduces a new parameterized type of hierarchical clustering algorithm called Versatile Linkage [[2](#references)], which includes Singles Linkage, Complete Linkage and Arithmetic Linkage as particular cases, and which naturally defines two new algorithms, Geometric Linkage and Harmonic Linkage (hence the convenience to rename UPGMA as Arithmetic Linkage, to emphasize the existence of different types of averages).

Similar functionality can also be obtained using package [**mdendro**](https://webs-deim.urv.cat/~sergio.gomez/mdendro.php) for the R language.


## Comparison with other applications

How do other applications deal with ties?

- Ignore ties, uncommented in their respective manuals: Mathematica, Matlab, R, Stats

- Report the existence of ties, and break them using the order of the observations in the input file: SAS

- Break ties using the order of cases in the input file, and recommend the comparison with cases sorted in different random orders: SPSS Statistics

How do I know if there are ties in my data?

- Most people would say *I do not have problems with tied distances*, however you cannot be sure unless the used software explicitly tells you so.

- In **MultiDendrograms** tied distances can be easily noticed in the dendrogram plots, in the dendrogram navigation window, and in the exported tree files.

How many binary dendrograms may correspond to one *MultiDendrogram*?

- 6 binary dendrograms for Table 3 in Fatahi et al, Vitis 42 (2003) 185-192.

- 36 binary dendrograms for Supplementary Table 2 in Zduni&#263; et al, Am. J. Enol. Vitic. 64 (2013) 285-290.

- 17900 binary dendrograms for Table 2 in Ib&aacute;&ntilde;ez et al, Am. J. Enol. Vitic. 54 (2003) 22-30.

- 760590880 binary dendrograms in Almadanim et al, Vitis 46 (2007) 116-119.

You may use the *Hierarchical_Clustering* program in [**Radatools**](https://webs-deim.urv.cat/~sergio.gomez/radatools.php) to count how many binary dendrograms correspond to your data.


## Documentation

The last version of the *Manual* is always available at the [*Download*](https://webs-deim.urv.cat/~sergio.gomez/multidendrograms.php#download) section of **MultiDendrograms** [home page](https://webs-deim.urv.cat/~sergio.gomez/multidendrograms.php)


## References

[1] Alberto Fern&aacute;ndez and Sergio G&oacute;mez: Solving Non-uniqueness in Agglomerative Hierarchical Clustering Using Multidendrograms, *Journal of Classification* **25** (2008) 43-65 ([pdf](https://webs-deim.urv.cat/~sergio.gomez/papers/Fernandez-Solving_non-uniqueness_in_AHC_using_multidendrograms.pdf)) ([doi](https://doi.org/10.1007/s00357-008-9004-x)) ([Springer](https://link.springer.com/article/10.1007/s00357-008-9004-x))

[2] Alberto Fern&aacute;ndez and Sergio G&oacute;mez: Versatile linkage: a family of space-conserving strategies for agglomerative hierarchical clustering, *Journal of Classification* **37** (2020) 584-597 ([pdf](https://webs-deim.urv.cat/~sergio.gomez/papers/Fernandez-Versatile_linkage-A_family_of_space-conserving_strategies_for_AHC.pdf)) ([doi](https://doi.org/10.1007/s00357-019-09339-z)) ([Springer](https://link.springer.com/article/10.1007/s00357-019-09339-z))


## Webs

- **MultiDendrograms** home: https://webs-deim.urv.cat/~sergio.gomez/multidendrograms.php and [GitHub](https://github.com/sergio-gomez/MultiDendrograms)
- **mdendro** home: https://webs-deim.urv.cat/~sergio.gomez/mdendro.php and [GitHub](https://github.com/sergio-gomez/mdendro)
- **Radatools** home: https://webs-deim.urv.cat/~sergio.gomez/radatools.php, with source code as [**Radalib**](https://webs-deim.urv.cat/~sergio.gomez/radalib.php) at [GitHub](https://github.com/sergio-gomez/Radalib)


## Authors

- **Alberto Fern&aacute;ndez**: Dept. Enginyeria Qu&iacute;mica, Universitat Rovira i Virgili, Tarragona (Spain). ([email](mailto:alberto.fernandez@urv.cat?subject=[mdendro])) ([ORCID](https://orcid.org/0000-0002-1241-1646)) ([Google Scholar](https://scholar.google.es/citations?user=AbH4r0IAAAAJ)) ([GitHub](https://github.com/albyfs))

- **Sergio G&oacute;mez**: Dept. Enginyeria Inform&agrave;tica i Matem&agrave;tiques, Universitat Rovira i Virgili, Tarragona (Spain). ([web](https://webs-deim.urv.cat/~sergio.gomez/)) ([email](mailto:sergio.gomez@urv.cat?subject=[mdendro])) ([ORCID](http://orcid.org/0000-0003-1820-0062)) ([Google Scholar](https://scholar.google.es/citations?user=ETrjkSIAAAAJ)) ([GitHub](https://github.com/sergio-gomez)) ([Twitter](https://twitter.com/SergioGomezJ))

