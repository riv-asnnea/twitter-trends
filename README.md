# twitter-trends

This Project is used to retrieve the current trends for various locations. Locations are referenced using WOEIDs. The locations are stored locally in a Cassandra data store and are used to retrieve trending data for those specific locations.

The locations JSON can be found in the file  'woeidfile.txt'

By default, the WOEID for 'worldwide' is 1 and is the default value for the application.

#TODO integrate services into a UI.