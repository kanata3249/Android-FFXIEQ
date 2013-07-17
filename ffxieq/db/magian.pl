use DBI;
use Encode;

my $dbname='master.db';
my $datasource="dbi:SQLite:dbname=$dbname";
my $dbh = DBI->connect($datasource);

$lang = "jp";
$lb = "（";
$rb = "）";
removeold();				# remove old data		(delete maijian data in Equipment)
cre();						# build					Maijian => Equipment

$lang = "en";
$lb = "(";
$rb = ")";

removeold();				# remove old data		(delete maijian data in Equipment)
cre();						# build					Maijian => Equipment

$dbh->disconnect();

exit;

# create base table
# create aug table
# create combination table


sub cre
{
	my $sth = $dbh->prepare("select * from Magian_$lang order by _id");
	my $nid;
	my @row;

local $| = 1;
	$sth->execute();
	$nid = 40000;
	$dbh->{AutoCommit} = 0;  # enable transactions, if possible
	while (@row = $sth->fetchrow_array) {
		my ($mid, $baseID, $prefix, $postfix, $name, $level, $adesc) = @row;
		$name =~ s/\'/\'\'/g;

		$bsth = $dbh->prepare("select * from Equipment_$lang where Name LIKE '$name' AND Lv='$level'");
		$bsth->execute();
		if (@row = $bsth->fetchrow_array) {
			my ($id, $name, $part, $weapon, $job, $race, $level, $itemlevel, $rare, $ex, $desc_orig, $desc) = @row;
			
			if ($mid >= 10000) {
				$name = "$name$lb$prefix$postfix$rb";
			} else {
				$name = "$name$lb$prefix$mid$postfix$rb";
			}
			$name =~ s/\'/\'\'/g;
			$desc =~ s/[\r\n]+/\n/g;
			if (length($adesc) > 0) {
				$desc =~ s/\s+$//g;
				$desc = "$desc\n$adesc";
			}
			$desc =~ s/\'/\'\'/g;
			$desc_orig =~ s/\'/\'\'/g;
#			$desc =~ s/\n/\\n/g;
#			$desc_orig = $desc;
			$nid = $mid + 40000;
			print Encode::encode('cp932', decode_utf8("$mid $name $level                  \r"));
			$dbh->do("Insert into Equipment_$lang (_id, Name, Part, Weapon, Job, Race, Lv, ItemLv, Rare, Ex, DescriptionOrg, Description) VALUES ('$nid', '$name', '$part', '$weapon', '$job', '$race', '$level', '$itemlevel', '$rare', '$ex', '$desc_orig', '$desc')");
		}
	}
	
	 $dbh->commit;
}


sub removeold
{
	$dbh->do("delete From Equipment_$lang where Name LIKE '%（%'");
	$dbh->do("delete From Equipment_$lang where Name LIKE '%(%'");
}



