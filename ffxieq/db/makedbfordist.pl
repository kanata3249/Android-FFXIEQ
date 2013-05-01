use DBI;
use Encode;
use File::Copy 'copy';

my $master = "master.db";

exit main(@ARGV);



sub main
{
	$dbname = "ffxieq_jp.db";
	
	copy $master, $dbname;
	builddb($dbname, "jp", "en");

	$dbname = "ffxieq_en.db";
	copy $master, $dbname;
	builddb($dbname, "en", "jp");

	0;
}

sub builddb
{
	my ($dbname, $lang, $lang2) = @_;
	my $datasource;
	my $dbh;

	$datasource="dbi:SQLite:dbname=$dbname";
	$dbh = DBI->connect($datasource);


	$dbh->do("drop table JobNames");
	$dbh->do("drop table JobGroups");
	$dbh->do("drop table Types");
	$dbh->do("drop table Parts");
	$dbh->do("drop table tmp");

	$dbh->do("drop table Magian_$lang");
	$dbh->do("drop table Magian_$lang2");
	$dbh->do("drop table original_$lang");
	$dbh->do("drop table original_$lang2");

	$dbh->do("drop table Equipment_$lang2");
	$dbh->do("drop table JobTrait_$lang2");
	$dbh->do("drop table Strings_$lang2");
	$dbh->do("drop table Combination_$lang2");
	$dbh->do("drop table BlueMagic_$lang2");
	$dbh->do("drop table BlueMagicCombination_$lang2");
	$dbh->do("drop table Atma_$lang2");
	$dbh->do("drop table VWAtma_$lang2");
	$dbh->do("drop table MeritPoint_$lang2");

# rename
	$dbh->do("alter table JobTrait_$lang rename to JobTrait");
	$dbh->do("alter table Equipment_$lang rename to Equipment");
	$dbh->do("alter table Strings_$lang rename to Strings");
	$dbh->do("alter table Combination_$lang rename to Combination");
	$dbh->do("alter table BlueMagic_$lang rename to BlueMagic");
	$dbh->do("alter table BlueMagicCombination_$lang rename to BlueMagicCombination");
	$dbh->do("alter table Atma_$lang rename to Atma");
	$dbh->do("alter table VWAtma_$lang rename to VWAtma");
	$dbh->do("alter table MeritPoint_$lang rename to MeritPoint");
	
	$dbh->do("update Equipment set DescriptionOrg = NULL");
	$dbh->do("update Atma set DescriptionOrg = NULL");

	$dbh->disconnect();
}
