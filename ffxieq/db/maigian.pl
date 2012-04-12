use DBI;

my $dbname='master.db';
my $datasource="dbi:SQLite:dbname=$dbname";
my $dbh = DBI->connect($datasource);

#reve();					# reverse engineering	Equipment => Maigian
removeold();				# remove old data		(delete maijian data in Equipment)
cre();						# build					Maijian => Equipment

$dbh->disconnect();

exit;

# create base table
# create aug table
# create combination table


sub cre
{
	my $sth = $dbh->prepare("select * from Maigian order by _id");
	my $nid;
	my @row;

	$sth->execute();
	$nid = 40000;
	while (@row = $sth->fetchrow_array) {
		my ($mid, $prefix, $postfix, $name, $level, $adesc) = @row;
		$bsth = $dbh->prepare("select * from Equipment where Name LIKE '$name' AND Lv='$level'");
		$bsth->execute();
		if (@row = $bsth->fetchrow_array) {
			my ($id, $name, $part, $weapon, $job, $race, $level, $rare, $ex, $desc_orig, $desc) = @row;
			
			$name = "$name（$prefix$mid$postfix）";
			$desc =~ s/[\r\n]+/\n/g;
			if (length($adesc) > 0) {
				$desc =~ s/\s+$//g;
				$desc = "$desc\n$adesc";
			}
#			$desc =~ s/\n/\\n/g;
#			$desc_orig = $desc;
			$nid = $mid + 40000;
			print "$mid $name $level\n";
			$dbh->do("Insert into Equipment (_id, Name, Part, Weapon, Job, Race, Lv, Rare, Ex, DescriptionOrg, Description) VALUES ('$nid', '$name', '$part', '$weapon', '$job', '$race', '$level', '$rare', '$ex', '$desc_orig', '$desc')");
		}
	}
}



sub reve
{
	my $select = "select * from Equipment where Name LIKE '%（%'";
	my $sth = $dbh->prepare($select);
	$sth->execute();

	$nid = 20000;
	while (my @row = $sth->fetchrow_array) {
		my ($id, $name, $part, $weapon, $job, $race, $level, $rare, $ex, $desc_orig, $desc) = @row;
		my ($mid);
		
		if ($name =~ /^(.*)（([^0-9]*)([0-9]+)([^0-9]*)）/) {
			$mid = $3;
			$name = $1;
			$prefix = $2;
			$postfix = $4;
			
			$desc = substr($desc, length($desc_orig));
			$desc =~ s/^\s+//g;
			$desc =~ s/\n/\\n/g;
		}

		$sth2 = $dbh->prepare("select _id from Equipment where Name LIKE '$name' and Lv='$level'");
		$sth2->execute();
		if (@row = $sth2->fetchrow_array) {
#			print "$level $name found $row[0]\n";
		} else {
			$dbh->do("Insert into Equipment (_id, Name, Part, Weapon, Job, Race, Lv, Rare, Ex, DescriptionOrg, Description) VALUES ('$nid', '$name', '$part', '$weapon', '$job', '$race', '$level', '$rare', '$ex', '$desc_orig', '$desc_orig')");
			$nid++;
		}

#		print "$mid	$prefix	$postfix	$name\	$level	$desc\n";
		$sth2 = $dbh->prepare("select _id from Maigian where _id=?");
		$sth2->execute($mid);
		if ($sth2->fetchrow_array) {
			$dbh->do("update Maigian SET _id='$mid', prefix='$prefix', postfix='$postfix', Name='$name', Level='$level', Desc='$desc' where _id='$mid'");
		} else {
			$dbh->do("Insert into Maigian VALUES ('$mid', '$prefix', '$postfix', '$name', '$level', '$desc')");
		}
	}
}

sub removeold
{
	$dbh->do("delete From Equipment where Name LIKE '%（%'");
	$dbh->do("delete From Equipment where Name LIKE '%(%'");
}



