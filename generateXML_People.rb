#!/usr/bin/ruby

require 'faker'
require 'builder'
require 'date'

file = File.new("people.xml", "wb")
xml = Builder::XmlMarkup.new( :target => file, :indent => 2 )

xml.instruct! :xml, :version => "1.1", :encoding => "US-ASCII"

xml.people do
	for i in 0..19
		xml.person("id" => "%04d" % (i+1)){
			xml.firstname Faker::Name.first_name
			xml.lastname Faker::Name.last_name
			xml.birthdate rand(DateTime.new(DateTime.now.year-90)...DateTime.new(DateTime.now.year-5))
			xml.healthprofile{
			 	xml.lastupdate rand(DateTime.now-12...DateTime.now)
			 	xml.weight weight = rand(50.1...120.9).round(2)
			 	xml.height height = rand(1.2...2.0).round(2)
			 	xml.bmi '%.2f' % (weight/(height*height)) 
			}
		}
	end
end

file.close
